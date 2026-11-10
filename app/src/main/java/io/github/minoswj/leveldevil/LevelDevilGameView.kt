/*
 * Copyright (c) 2026 MinOS-WJ
 * https://github.com/MinOS-WJ
 * SPDX-License-Identifier: MIT
 */

package io.github.minoswj.leveldevil

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import java.io.ByteArrayInputStream

/**
 * Hardened, offline-only WebView for the packaged game.
 *
 * Only files under the bundled `/assets/game/` path can be loaded. HTTP(S)
 * navigation, popups, permissions, and remote resources are denied.
 */
@SuppressLint("SetJavaScriptEnabled")
internal class LevelDevilGameView(context: Context) : WebView(context) {
    private val assetLoader = WebViewAssetLoader.Builder()
        .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
        .build()

    init {
        setBackgroundColor(GAME_BACKGROUND)
        configureSettings()
        configureClients()
        overScrollMode = View.OVER_SCROLL_NEVER
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false
        isFocusable = true
        isFocusableInTouchMode = true
    }

    fun loadGame() {
        loadUrl(GAME_URL)
    }

    fun setDebuggingEnabled(enabled: Boolean) {
        setWebContentsDebuggingEnabled(enabled)
    }

    @Suppress("DEPRECATION")
    private fun configureSettings() = with(settings) {
        javaScriptEnabled = true
        domStorageEnabled = true
        databaseEnabled = false
        mediaPlaybackRequiresUserGesture = false
        allowFileAccess = false
        allowContentAccess = false
        allowFileAccessFromFileURLs = false
        allowUniversalAccessFromFileURLs = false
        javaScriptCanOpenWindowsAutomatically = false
        setSupportMultipleWindows(false)
        setSupportZoom(false)
        builtInZoomControls = false
        displayZoomControls = false
        setGeolocationEnabled(false)
        saveFormData = false
        savePassword = false
        mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        textZoom = 100
        userAgentString = "$userAgentString LevelDevilAndroid/1.1.2 MinOS-WJ"
    }

    private fun configureClients() {
        webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                val uri = request.url
                if (!isHttpScheme(uri)) return null

                if (isBundledGameUrl(uri)) {
                    return assetLoader.shouldInterceptRequest(uri) ?: blockedResponse()
                }
                return blockedResponse()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean = !isBundledGameUrl(request.url)

            override fun onReceivedSslError(
                view: WebView,
                handler: android.webkit.SslErrorHandler,
                error: android.net.http.SslError
            ) {
                handler.cancel()
            }
        }

        webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: android.os.Message
            ): Boolean = false

            override fun onPermissionRequest(request: PermissionRequest) {
                request.deny()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setRendererPriorityPolicy(RENDERER_PRIORITY_IMPORTANT, true)
        }
    }

    private fun isBundledGameUrl(uri: Uri): Boolean =
        uri.scheme == "https" &&
            uri.host == APP_ASSETS_HOST &&
            uri.path?.startsWith(GAME_ASSET_PATH) == true

    private fun isHttpScheme(uri: Uri): Boolean =
        uri.scheme == "https" || uri.scheme == "http"

    private fun blockedResponse(): WebResourceResponse = WebResourceResponse(
        "text/plain",
        "utf-8",
        403,
        "Blocked by single-player policy",
        mapOf("Cache-Control" to "no-store"),
        ByteArrayInputStream(ByteArray(0))
    )

    companion object {
        private const val APP_ASSETS_HOST = "appassets.androidplatform.net"
        private const val GAME_ASSET_PATH = "/assets/game/"
        private const val GAME_URL = "https://$APP_ASSETS_HOST${GAME_ASSET_PATH}index.html"
        private val GAME_BACKGROUND = Color.rgb(44, 7, 1)
    }
}

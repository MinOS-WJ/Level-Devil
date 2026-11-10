/*
 * Copyright (c) 2026 MinOS-WJ
 * https://github.com/MinOS-WJ
 * SPDX-License-Identifier: MIT
 */

package io.github.minoswj.leveldevil

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Single-player entry point for the bundled Level Devil 1.1.2 build.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var gameView: LevelDevilGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        configureDisplayCutout()

        gameView = LevelDevilGameView(this).apply {
            setDebuggingEnabled(applicationInfo.isDebuggable())
        }
        setContentView(gameView)

        val restored = savedInstanceState != null && gameView.restoreState(savedInstanceState) != null
        if (!restored) gameView.loadGame()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (gameView.canGoBack()) {
                        gameView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )

        hideSystemBars()
    }

    override fun onResume() {
        super.onResume()
        gameView.onResume()
        hideSystemBars()
    }

    override fun onPause() {
        gameView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        gameView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    override fun onDestroy() {
        if (::gameView.isInitialized) {
            (gameView.parent as? ViewGroup)?.removeView(gameView)
            gameView.destroy()
        }
        super.onDestroy()
    }

    private fun hideSystemBars() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }

    private fun configureDisplayCutout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
    }

    private fun ApplicationInfo.isDebuggable(): Boolean =
        flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}

/*
 * Copyright (c) 2026 MinOS-WJ
 * https://github.com/MinOS-WJ
 * SPDX-License-Identifier: MIT
 *
 * Offline single-player PokiSDK compatibility layer.
 *
 * The original game was built for the browser/Poki platform. This object keeps
 * the game's required JavaScript API intact while removing ads, accounts,
 * leaderboards, external links, telemetry, and all network activity.
 */
(() => {
  'use strict';

  const noop = () => {};
  const resolveTrue = () => Promise.resolve(true);
  const resolveEmptyArray = () => Promise.resolve([]);
  const resolveEmptyObject = () => Promise.resolve({});

  window.PokiSDK = Object.freeze({
    // Lifecycle and loading
    init: resolveTrue,
    initWithVideoHB: resolveTrue,
    gameLoadingStart: noop,
    gameLoadingProgress: noop,
    gameLoadingFinished: noop,
    gameInteractive: noop,
    gameplayStart: noop,
    gameplayStop: noop,
    roundStart: noop,
    roundEnd: noop,

    // Ads are never displayed
    commercialBreak: resolveTrue,
    rewardedBreak: () => Promise.resolve(false),
    displayAd: noop,
    destroyAd: noop,
    isAdBlocked: () => true,
    muteAd: noop,

    // Accounts, social features, and leaderboards are disabled
    getLeaderboard: resolveEmptyArray,
    showLeaderboard: noop,
    getUser: resolveEmptyObject,
    getToken: () => Promise.resolve(null),
    login: () => Promise.reject(new Error('Online accounts are disabled')),
    sendHighscore: noop,
    shareableURL: () => Promise.resolve({ url: window.location.href }),
    generateScreenshot: () => Promise.resolve(null),

    // Local platform information only
    getLanguage: () => navigator.language.split('-')[0] || 'en',
    getIsoLanguage: () => undefined,
    getURLParam: () => '',

    // Telemetry and diagnostics stay local and are discarded
    setDebug: noop,
    setLogging: noop,
    setPlayerAge: noop,
    enableEventTracking: noop,
    measure: noop,
    captureError: noop,
    logError: noop,
    customEvent: () => ({ doNothing: noop }),
    setDebugTouchOverlayController: noop,
    setPlaytestCanvas: noop,
    playtestSetCanvas: noop,
    playtestCaptureHtmlOnce: noop,
    playtestCaptureHtmlOn: noop,
    playtestCaptureHtmlOff: noop,
    movePill: noop,

    // Never leave the packaged game
    openExternalLink: noop
  });
})();

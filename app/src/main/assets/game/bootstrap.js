/*
 * Copyright (c) 2026 MinOS-WJ
 * https://github.com/MinOS-WJ
 * SPDX-License-Identifier: MIT
 */
(function () {
  'use strict';

  // The Android build is an offline, single-player product. Do not allow the
  // WebView document to open links, menus, or external windows.
  document.addEventListener('contextmenu', function (event) {
    event.preventDefault();
  }, false);

  document.addEventListener('click', function (event) {
    var target = event.target;
    if (target && target.closest && target.closest('a[href]')) {
      event.preventDefault();
    }
  }, false);

  Module['onRuntimeInitialized'] = function () {
    Module.runApp('canvas');
  };

  EngineLoader.load('canvas', 'LevelDevil');
})();

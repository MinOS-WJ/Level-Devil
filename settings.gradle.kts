/*
 * Copyright (c) 2026 MinOS-WJ
 * https://github.com/MinOS-WJ
 * SPDX-License-Identifier: MIT
 */

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Level-Devil"
include(":app")

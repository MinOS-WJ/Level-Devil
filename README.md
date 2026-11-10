<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" width="128" height="128" alt="Level-Devil icon">
</p>

<h1 align="center">Level-Devil</h1>

<p align="center">
  离线单人版 Level Devil Android 应用<br>
  Android 6.0+ · WebView · WebAssembly · No Ads · No Network Permission
</p>

<p align="center">
  <a href="https://github.com/MinOS-WJ/Level-Devil/releases/latest">下载最新版本</a>
  ·
  <a href="https://github.com/MinOS-WJ/Level-Devil/issues">问题反馈</a>
</p>

## 项目简介

本仓库由 [MinOS-WJ](https://github.com/MinOS-WJ) 维护，将本地打包的
**Level Devil 1.1.2** WebAssembly 游戏封装为 Android 应用。

应用仅保留单人模式，所有游戏资源随 APK 一起分发，不依赖远程游戏服务器。

## 功能特性

- 仅保留 `1 Player` 单人模式，多入口已移出可视区域。
- 完全离线运行，Android 清单不声明 `INTERNET` 权限。
- 无广告、账号登录、排行榜、遥测及外部跳转。
- 使用 `WebViewAssetLoader` 从 APK 内加载 HTML、WASM 和归档资源。
- WebView 仅允许访问内置 `/assets/game/`，其他 HTTP(S) 请求一律拦截。
- 沉浸式横屏、全屏显示，支持 Android 6.0（API 23）及以上。
- 通过 `keystore.properties` 配置 Release 签名，密钥和密码不会提交到仓库。

## 下载

前往 [GitHub Releases](https://github.com/MinOS-WJ/Level-Devil/releases/latest)
下载已签名 APK。

本地构建输出：

```text
dist/Level-Devil-1.1.2-debug.apk
dist/Level-Devil-1.1.2-release.apk
```

`dist/` 中的 APK 属于本地构建产物，不提交到源码仓库。

## 环境要求

- Android Studio
- Android SDK 35
- JDK 17

## 构建

Windows PowerShell：

```powershell
.\gradlew.bat assembleDebug --offline
.\gradlew.bat assembleRelease --offline
```

调试 APK：

```text
app/build/outputs/apk/debug/app-debug.apk
```

Release APK：

```text
app/build/outputs/apk/release/app-release.apk
```

## Release 签名

签名文件不会进入 Git 仓库。需要发布新版本时：

1. 将 `keystore.properties.example` 复制为 `keystore.properties`。
2. 填写密钥库路径、密码、别名和 Key 密码。
3. 执行 `.\gradlew.bat assembleRelease --offline`。

```properties
storeFile=release-key/leveldevil.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=leveldevil
keyPassword=YOUR_KEY_PASSWORD
```

`keystore.properties`、`release-key/`、`*.jks` 和 `*.keystore` 均已被
`.gitignore` 排除。请安全备份签名密钥，后续版本必须继续使用同一密钥。

## 项目结构

```text
app/src/main/java/io/github/minoswj/leveldevil/
├── MainActivity.kt           # Activity、生命周期、沉浸式全屏
└── LevelDevilGameView.kt     # 离线 WebView、资源与导航限制

app/src/main/assets/game/
├── index.html                # Web 运行入口
├── bootstrap.js              # WASM 启动
├── sdk.js                    # 离线单人 SDK 兼容层
├── dmloader.js               # Defold 加载器
└── archive/                  # Level Devil 1.1.2 游戏归档

app/src/main/res/             # 应用图标、主题和字符串资源
dist/                         # 本地 APK 输出（不提交）
```

应用 ID 与 Kotlin 包名均为 `io.github.minoswj.leveldevil`。

## 隐私与安全

- 应用不申请网络权限。
- WebView 不加载远程内容。
- 外部链接、弹窗、文件访问和权限请求均被禁止。
- 本地 SDK 兼容层不发送广告、遥测或用户数据。

## 版权与许可

Android 封装代码版权归 [MinOS-WJ](https://github.com/MinOS-WJ) 所有，
使用 [MIT License](LICENSE)。

Level Devil 游戏本体、图片、音频、名称及相关商标属于其各自权利人
（包括 Denda Games 与 Unept），**不适用本仓库的 MIT 许可**。详见
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

分发或商业使用前，请自行确认并取得游戏内容所需的第三方授权。

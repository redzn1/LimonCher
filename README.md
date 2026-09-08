# LimonCher

> Modern Minecraft: Java Edition launcher for Android.

**Version:** `0.6.3-AndroidVer`  
**Package:** `com.limone.limoncher`  
**Developer:** **Limone Production**  
**License:** GPL-3.0 (see [`LICENSE`](LICENSE))

## ✨ Highlights

- Minecraft: Java Edition launcher experience redesigned for Android
- Compact Minecraft Launcher-inspired navigation and responsive layouts
- Microsoft, Ely.by and Offline account support
- Skin/cape support, including imported PNG capes where supported
- Minecraft version installation and download management
- Mods/resource downloads through the built-in download system
- In-app GitHub release update checking
- Launcher update marker: releases containing `-lcto-`
- ARM, ARM64, x86, x86_64 and Universal APK builds
- Data-preserving updates: game data is not intentionally wiped by the launcher update flow
- Android 8.0+ (`minSdk 26`), target SDK 35, compile SDK 37

## 🧭 Interface

The 0.6.x interface uses a compact dark launcher shell with:

- **HOME**
- **MINECRAFT: JAVA EDITION**
- **Mods** in the Java Edition navigation
- **Settings**
- account/profile access from the top-right
- responsive drawer behavior on smaller screens

The original launcher/game-management capabilities remain underneath the redesigned presentation.

## 🛠️ Build locally

Requirements:

- JDK 17
- Android SDK Platform 37
- Android Build Tools 35.0.0
- NDK 25.2.9519653

```bash
chmod +x ./gradlew
./gradlew test :LimonCher:assembleDebug
```

For an architecture-specific release build:

```bash
./gradlew :LimonCher:assembleRelease -Parch=arm64
```

Available values for `arch` are `arm`, `arm64`, `x86`, `x86_64`, and `all`.

## 🤖 GitHub Actions

The repository uses `.github/workflows/build.yml`.

The workflow:

1. checks out the repository and submodules;
2. installs JDK 17 and Android SDK components;
3. installs compile SDK 37, build tools 35.0.0 and NDK 25.2.9519653;
4. builds/tests every supported architecture;
5. uploads APK artifacts;
6. publishes APKs when a `v*` tag is pushed.

### Release signing

For production in-place updates, all releases must use the **same Android signing certificate** and a monotonically increasing `versionCode`. Configure these GitHub Actions secrets:

- `LIMONCHER_KEYSTORE_B64`
- `LIMONCHER_STORE_PASSWORD`
- `LIMONCHER_KEY_PASSWORD`
- `LIMONCHER_KEY_ALIAS`

Never commit the keystore or passwords to the repository.

## 🔄 In-app launcher updates

LimonCher checks GitHub Releases asynchronously. A release is considered a launcher update when its release notes contain the marker:

```text
-lcto-
```

The intended flow is **Download → Verify → Install → Preserve data → Restart**. If GitHub is unavailable, the launcher should remain usable and expose retry/error states instead of blocking the launcher.

## 🗂️ Data safety

Updating the launcher must not intentionally remove existing Minecraft data such as:

- worlds/saves
- mods
- resource packs
- screenshots
- configurations
- accounts/profiles

A destructive reset should only happen through an explicit user action.

## 📜 Licensing & attribution

LimonCher is distributed under GPL-3.0. Third-party components retain their own copyright notices and license requirements. See [`LICENSE`](LICENSE) and the relevant module/license files before redistributing modified builds.

## 👨‍💻 Project

- App name: **LimonCher**
- Version: **0.6.3-AndroidVer**
- Developer: **Limone Production**
- Application ID: `com.limone.limoncher`

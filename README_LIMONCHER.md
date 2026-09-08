# LimonCher

LimonCher is the Android Minecraft Java launcher derived from the supplied launcher source, with preserved launcher/game-management logic and a refreshed LimonCher visual identity.

## Identity

- Application: LimonCher
- Package: `com.limone.limoncher`
- Version: `0.5.2-AndroidVer`
- Version code: `24`
- Build label: `052-AndroidVer-LimonCher`
- Developer: Limone Production X RedzDev

## Authentication

LimonCher keeps Microsoft authentication, offline/local accounts, and the existing generic Yggdrasil authentication server system. Ely.by is seeded into the account manager automatically using its official authentication endpoint, while game launches use the dedicated authlib-injector endpoint.

## Local build

Windows PowerShell:

```powershell
.\gradlew.bat clean assembleDebug
```

Linux/macOS:

```bash
./gradlew clean assembleDebug
```

For an architecture-specific build:

```bash
./gradlew :LimonCher:assembleDebug -Darch=arm
./gradlew :LimonCher:assembleDebug -Darch=arm64
./gradlew :LimonCher:assembleDebug -Darch=x86
./gradlew :LimonCher:assembleDebug -Darch=x86_64
./gradlew :LimonCher:assembleDebug -Darch=all
```

## GitHub Actions

`.github/workflows/limoncher-build.yml` builds five matrix targets:

- ARM (`armeabi-v7a`)
- ARM64 (`arm64-v8a`)
- x86
- x86_64
- Universal (all ABIs)

Pushing a tag such as `v0.3.8` also publishes the matrix APKs to the GitHub Release for that tag.

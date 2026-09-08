# LimonCher Build Guide

## Local debug

```powershell
.\gradlew.bat :LimonCher:assembleDebug -Darch=arm
```

Supported arch values: `arm`, `arm64`, `x86`, `x86_64`, `all`.

## Local release

Release signing is optional locally. Without a release keystore/password, Gradle produces an unsigned release APK.

For a signed local release, provide:

- `limoncher-release.jks` inside `LimonCher/`
- `STORE_PASSWORD`
- `KEY_PASSWORD`
- optional `KEY_ALIAS` (defaults to `limoncher`)

## GitHub release

Create these repository Actions secrets:

- `No signing secrets required`
- `No signing secrets required`
- `No signing secrets required`
- `No signing secrets required`

Then push a tag such as `v0.3.8`. GitHub Actions builds ARM, ARM64, x86, x86_64 and Universal and publishes all APKs to the GitHub Release automatically.

# LimonCher In-App Updates

LimonCher checks the latest GitHub Release from `redzn1/LimonCher`.
When a newer release is detected, the launcher selects the APK matching the
current device ABI, downloads it into the private cache, and opens Android's
package installer. A normal package update keeps the existing app data and
installed game data; the user does not need to manually uninstall or browse for
an APK.

Release APKs use the bundled LimonCher signing key so successive LimonCher
releases remain update-compatible. Keep this key unchanged for future releases.

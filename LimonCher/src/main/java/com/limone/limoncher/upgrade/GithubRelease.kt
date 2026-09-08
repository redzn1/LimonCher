package com.limone.limoncher.upgrade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    @SerialName("tag_name") val tagName: String,
    @SerialName("name") val name: String? = null,
    @SerialName("body") val body: String? = null,
    @SerialName("published_at") val publishedAt: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    @SerialName("assets") val assets: List<Asset> = emptyList()
) {
    @Serializable
    data class Asset(
        @SerialName("name") val name: String,
        @SerialName("browser_download_url") val browserDownloadUrl: String,
        @SerialName("size") val size: Long = 0L,
        @SerialName("content_type") val contentType: String? = null
    )
}

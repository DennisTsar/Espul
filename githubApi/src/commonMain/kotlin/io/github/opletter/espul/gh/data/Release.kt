package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    val assets: List<Asset>,
    @SerialName("assets_url")
    val assetsUrl: String,
    val author: User,
    // optional in docs
    val body: String?,
//    @SerialName("body_html")
//    val bodyHtml: String? = null,
//    @SerialName("body_text")
//    val bodyText: String? = null,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("discussion_url")
    val discussionUrl: String? = null,
    val draft: Boolean,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Int,
    @SerialName("mentions_count")
    val mentionsCount: Int? = null,
    val name: String?,
    @SerialName("node_id")
    val nodeId: String,
    val prerelease: Boolean,
    @SerialName("published_at")
    val publishedAt: Instant?,
    val reactions: Reactions? = null,
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("tarball_url")
    val tarballUrl: String?,
    @SerialName("target_commitish")
    val targetCommitish: String,
    @SerialName("upload_url")
    val uploadUrl: String,
    val url: String,
    @SerialName("zipball_url")
    val zipballUrl: String?,
    // not in docs
    @SerialName("short_description_html")
    val shortDescriptionHtml: String,
    // not in docs
    @SerialName("is_short_description_html_truncated")
    val isShortDescriptionHtmlTruncated: Boolean,
    // not in docs
    val mentions: List<Mention> = emptyList(),
)

@Serializable
data class Mention(
    @SerialName("avatar_url")
    val avatarUrl: String,
    val login: String,
    @SerialName("profile_name")
    val profileName: String?,
    @SerialName("profile_url")
    val profileUrl: String,
    @SerialName("avatar_user_actor")
    val avatarUserActor: Boolean,
)

@Serializable
data class Asset(
    @SerialName("browser_download_url")
    val browserDownloadUrl: String,
    @SerialName("content_type")
    val contentType: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("download_count")
    val downloadCount: Int,
    val id: Int,
    val label: String?,
    val name: String,
    @SerialName("node_id")
    val nodeId: String,
    val size: Int,
    val state: AssetState,
    @SerialName("updated_at")
    val updatedAt: Instant,
    // nullable in docs
    val uploader: User,
    val url: String,
)

enum class AssetState {
    @SerialName("uploaded")
    UPLOADED,

    @SerialName("open")
    OPEN,
}

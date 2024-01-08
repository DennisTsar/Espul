package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PRComment(
    @SerialName("author_association")
    val authorAssociation: AuthorAssociation,
    val body: String,
//    @SerialName("body_html")
//    val bodyHtml: String? = null,
//    @SerialName("body_text")
//    val bodyText: String? = null,
    @SerialName("commit_id")
    val commitId: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("diff_hunk")
    val diffHunk: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Int,
    @SerialName("in_reply_to_id")
    val inReplyToId: Int? = null,
    // optional in docs
    val line: Int?,
    @SerialName("_links")
    val links: PRCommentLinks,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("original_commit_id")
    val originalCommitId: String,
    // optional in docs
    @SerialName("original_line")
    val originalLine: Int?,
    // optional in docs
    @SerialName("original_position")
    val originalPosition: Int?,
    // optional in docs
    @SerialName("original_start_line")
    val originalStartLine: Int?,
    val path: String,
    // optional in docs
    val position: Int?,
    // nullable in docs
    @SerialName("pull_request_review_id")
    val pullRequestReviewId: Int,
    @SerialName("pull_request_url")
    val pullRequestUrl: String,
    // optional in docs
    val reactions: Reactions,
    // optional in docs
    val side: StartSide,
    // optional in docs
    @SerialName("start_line")
    val startLine: Int?,
    // optional in docs
    @SerialName("start_side")
    val startSide: StartSide?,
    @SerialName("subject_type")
    val subjectType: SubjectType? = null,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
    val user: User,
)

@Serializable
data class PRCommentLinks(
    val html: UrlObject,
    @SerialName("pull_request")
    val pullRequest: UrlObject,
    val self: UrlObject,
)

enum class StartSide {
    LEFT,
    RIGHT,
}

enum class SubjectType {
    @SerialName("line")
    LINE,

    @SerialName("file")
    FILE,
}

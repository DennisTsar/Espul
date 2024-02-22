package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PRReview(
    @SerialName("author_association")
    val authorAssociation: AuthorAssociation,
    // not nullable in docs
    val body: String?,
//    @SerialName("body_html")
//    val bodyHtml: String? = null,
//    @SerialName("body_text")
//    val bodyText: String? = null,
    // nullable in docs
    @SerialName("commit_id")
    val commitId: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    @SerialName("_links")
    val links: PRReviewLinks,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("pull_request_url")
    val pullRequestUrl: String,
    val state: String,
    // optional in docs
    @SerialName("submitted_at")
    val submittedAt: Instant,
    val user: User,
)

@Serializable
data class PRReviewLinks(
    val html: UrlObject,
    @SerialName("pull_request")
    val pullRequest: UrlObject,
)

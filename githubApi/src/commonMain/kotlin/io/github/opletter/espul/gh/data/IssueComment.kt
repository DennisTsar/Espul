package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueComment(
    @SerialName("author_association")
    val authorAssociation: AuthorAssociation,
    // optional in docs
    val body: String,
//    @SerialName("body_html")
//    val bodyHtml: String? = null,
//    @SerialName("body_text")
//    val bodyText: String? = null,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    @SerialName("issue_url")
    val issueUrl: String,
    @SerialName("node_id")
    val nodeId: String,
    // optional in docs
    @SerialName("performed_via_github_app")
    val performedViaGithubApp: GithubApp?,
    // optional in docs
    val reactions: Reactions,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
    // nullable in docs
    val user: User,
)

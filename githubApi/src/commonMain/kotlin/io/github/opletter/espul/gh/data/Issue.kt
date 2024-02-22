package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    @SerialName("active_lock_reason")
    val activeLockReason: String?,
    val assignee: User?,
    val assignees: List<User>,
    @SerialName("author_association")
    val authorAssociation: AuthorAssociation,
    val body: String?,
//    @SerialName("body_html")
//    val bodyHtml: String? = null,
//    @SerialName("body_text")
//    val bodyText: String? = null,
    @SerialName("closed_at")
    val closedAt: Instant?,
//    @SerialName("closed_by")
//    val closedBy: User? = null,
    val comments: Int,
    @SerialName("comments_url")
    val commentsUrl: String,
    @SerialName("created_at")
    val createdAt: Instant,
    val draft: Boolean? = null,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    val labels: List<Label>,
    @SerialName("labels_url")
    val labelsUrl: String,
    // optional in docs
    val locked: Boolean,
    val milestone: Milestone?,
    @SerialName("node_id")
    val nodeId: String,
    val number: Int,
    // optional in docs
    @SerialName("performed_via_github_app")
    val performedViaGithubApp: GithubApp?,
    @SerialName("pull_request")
    val pullRequest: IssuePullRequest? = null,
    // optional in docs
    val reactions: Reactions,
//    val repository: JsonObject? = null,
    @SerialName("repository_url")
    val repositoryUrl: String,
    val state: OpenState,
    @SerialName("state_reason")
    val stateReason: IssueStateReason?,
    // optional in docs
    @SerialName("timeline_url")
    val timelineUrl: String,
    val title: String,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
    // nullable in docs
    val user: User,
)

enum class IssueStateReason {
    @SerialName("completed")
    COMPLETED,

    @SerialName("reopened")
    REOPENED,

    @SerialName("not_planned")
    NOT_PLANNED,
}

package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PullRequestSimple(
    // optional in docs
    @SerialName("active_lock_reason")
    val activeLockReason: String?,
    val assignee: User?,
    // nullable & optional in docs
    val assignees: List<User>,
    @SerialName("author_association")
    val authorAssociation: AuthorAssociation,
    @SerialName("auto_merge")
    val autoMerge: AutoMerge?,
    val base: RepoRef,
    val body: String?,
    @SerialName("closed_at")
    val closedAt: Instant?,
    @SerialName("comments_url")
    val commentsUrl: String,
    @SerialName("commits_url")
    val commitsUrl: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("diff_url")
    val diffUrl: String,
    // optional in docs
    val draft: Boolean,
    val head: RepoRef,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    @SerialName("issue_url")
    val issueUrl: String,
    val labels: List<Label>,
    @SerialName("_links")
    val links: PRLinks,
    val locked: Boolean,
    @SerialName("merge_commit_sha")
    val mergeCommitSha: String?,
    @SerialName("merged_at")
    val mergedAt: Instant?,
    val milestone: Milestone?,
    @SerialName("node_id")
    val nodeId: String,
    val number: Int,
    @SerialName("patch_url")
    val patchUrl: String,
    // nullable & optional in docs
    @SerialName("requested_reviewers")
    val requestedReviewers: List<User>,
    // nullable & optional in docs
    @SerialName("requested_teams")
    val requestedTeams: List<Team>,
    @SerialName("review_comment_url")
    val reviewCommentUrl: String,
    @SerialName("review_comments_url")
    val reviewCommentsUrl: String,
    val state: OpenState,
    @SerialName("statuses_url")
    val statusesUrl: String,
    val title: String,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
    val user: User,
)

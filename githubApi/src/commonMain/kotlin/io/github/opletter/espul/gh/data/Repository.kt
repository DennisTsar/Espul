package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// "Minimal Repository"
@Serializable
data class Repository(
    // optional in docs
    @SerialName("allow_forking")
    val allowForking: Boolean,
    @SerialName("archive_url")
    val archiveUrl: String,
    // optional in docs
    val archived: Boolean,
    @SerialName("assignees_url")
    val assigneesUrl: String,
    @SerialName("blobs_url")
    val blobsUrl: String,
    @SerialName("branches_url")
    val branchesUrl: String,
    // optional in docs
    @SerialName("clone_url")
    val cloneUrl: String,
//    @SerialName("code_of_conduct")
//    val codeOfConduct: CodeOfConduct? = null,
    @SerialName("collaborators_url")
    val collaboratorsUrl: String,
    @SerialName("comments_url")
    val commentsUrl: String,
    @SerialName("commits_url")
    val commitsUrl: String,
    @SerialName("compare_url")
    val compareUrl: String,
    @SerialName("contents_url")
    val contentsUrl: String,
    @SerialName("contributors_url")
    val contributorsUrl: String,
    // optional in docs
    @SerialName("created_at")
    val createdAt: Instant,
    // optional in docs
    @SerialName("default_branch")
    val defaultBranch: String,
//    @SerialName("delete_branch_on_merge")
//    val deleteBranchOnMerge: Boolean? = null,
    @SerialName("deployments_url")
    val deploymentsUrl: String,
    val description: String?,
    // optional in docs
    val disabled: Boolean,
    @SerialName("downloads_url")
    val downloadsUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    val fork: Boolean,
    // optional in docs
    val forks: Int,
    // optional in docs
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("forks_url")
    val forksUrl: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("git_commits_url")
    val gitCommitsUrl: String,
    @SerialName("git_refs_url")
    val gitRefsUrl: String,
    @SerialName("git_tags_url")
    val gitTagsUrl: String,
    // nullable in docs
    @SerialName("git_url")
    val gitUrl: String,
    // optional in docs
    @SerialName("has_discussions")
    val hasDiscussions: Boolean,
    // optional in docs
    @SerialName("has_downloads")
    val hasDownloads: Boolean,
    // optional in docs
    @SerialName("has_issues")
    val hasIssues: Boolean,
    // optional in docs
    @SerialName("has_pages")
    val hasPages: Boolean,
    // optional in docs
    @SerialName("has_projects")
    val hasProjects: Boolean,
    // optional in docs
    @SerialName("has_wiki")
    val hasWiki: Boolean,
    // optional in docs
    val homepage: String?,
    @SerialName("hooks_url")
    val hooksUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    // optional in docs
    @SerialName("is_template")
    val isTemplate: Boolean,
    @SerialName("issue_comment_url")
    val issueCommentUrl: String,
    @SerialName("issue_events_url")
    val issueEventsUrl: String,
    @SerialName("issues_url")
    val issuesUrl: String,
    @SerialName("keys_url")
    val keysUrl: String,
    @SerialName("labels_url")
    val labelsUrl: String,
    // optional in docs
    val language: String?,
    @SerialName("languages_url")
    val languagesUrl: String,
    // optional in docs
    val license: License?,
    @SerialName("merges_url")
    val mergesUrl: String,
    @SerialName("milestones_url")
    val milestonesUrl: String,
    // optional in docs
    @SerialName("mirror_url")
    val mirrorUrl: String?,
    val name: String,
//    @SerialName("network_count")
//    val networkCount: Int? = null,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("notifications_url")
    val notificationsUrl: String,
    // optional in docs
    @SerialName("open_issues")
    val openIssues: Int,
    // optional in docs
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val owner: User,
//    val permissions: Permissions? = null,
    @SerialName("private")
    val isPrivate: Boolean,
    // not present in some contexts
    @SerialName("public")
    val isPublic: Boolean? = null,
    @SerialName("pulls_url")
    val pullsUrl: String,
    // optional in docs
    @SerialName("pushed_at")
    val pushedAt: Instant,
    @SerialName("releases_url")
    val releasesUrl: String,
//    @SerialName("role_name")
//    val roleName: String? = null,
//    @SerialName("security_and_analysis")
//    val securityAndAnalysis: JsonObject? = null,
    // optional in docs
    val size: Int,
    // optional in docs
    @SerialName("ssh_url")
    val sshUrl: String,
    // optional in docs
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("stargazers_url")
    val stargazersUrl: String,
    @SerialName("statuses_url")
    val statusesUrl: String,
//    @SerialName("subscribers_count")
//    val subscribersCount: Int? = null,
    @SerialName("subscribers_url")
    val subscribersUrl: String,
    @SerialName("subscription_url")
    val subscriptionUrl: String,
    // optional in docs
    @SerialName("svn_url")
    val svnUrl: String,
    @SerialName("tags_url")
    val tagsUrl: String,
    @SerialName("teams_url")
    val teamsUrl: String,
//    @SerialName("temp_clone_token")
//    val tempCloneToken: String? = null,
    val topics: List<String>,
    @SerialName("trees_url")
    val treesUrl: String,
    // optional in docs
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
    // optional in docs
    val visibility: String,
    // optional in docs
    val watchers: Int,
    @SerialName("watchers_count")
    // optional in docs
    val watchersCount: Int,
    // optional in docs
    @SerialName("web_commit_signoff_required")
    val webCommitSignoffRequired: Boolean,
)

package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GithubEvent<out T : GithubEventPayload> {
    val actor: Actor

    @SerialName("created_at")
    val createdAt: Instant
    val id: String

    @SerialName("public")
    val isPublic: Boolean
    val org: Org?
    val payload: T
    val repo: Repo
    val type: String
}

sealed interface GithubEventPayload

@Serializable
@SerialName("CommitCommentEvent")
data class CommitCommentEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<CommitCommentEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String? = null, // Not nullable in docs // Can be `created`
        val comment: CommitComment,
    ) : GithubEventPayload
}

@Serializable
@SerialName("CreateEvent")
data class CreateEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<CreateEvent.Payload> {
    @Serializable
    data class Payload(
        val ref: String?,
        @SerialName("ref_type")
        val refType: RefType,
        @SerialName("master_branch")
        val masterBranch: String,
        val description: String?,
        @SerialName("pusher_type")
        val pusherType: String? = null, // Not in the docs
    ) : GithubEventPayload

    enum class RefType {
        @SerialName("branch")
        BRANCH,

        @SerialName("repository")
        REPOSITORY,

        @SerialName("tag")
        TAG,
    }
}

@Serializable
@SerialName("DeleteEvent")
data class DeleteEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<DeleteEvent.Payload> {
    @Serializable
    data class Payload(
        val ref: String,
        @SerialName("ref_type")
        val refType: RefType,
        @SerialName("pusher_type")
        val pusherType: String? = null, // Not in the docs
    ) : GithubEventPayload

    enum class RefType {
        @SerialName("branch")
        BRANCH,

        @SerialName("tag")
        TAG,
    }
}

@Serializable
@SerialName("ForkEvent")
data class ForkEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<ForkEvent.Payload> {
    @Serializable
    data class Payload(val forkee: RepoMin2) : GithubEventPayload
}

@Serializable
@SerialName("GollumEvent")
data class GollumEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<GollumEvent.Payload> {
    @Serializable
    data class Payload(val pages: List<Page>) : GithubEventPayload
}

@Serializable
@SerialName("IssueCommentEvent")
data class IssueCommentEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<IssueCommentEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be one of `created`, `edited`, or `deleted`
        val changes: String? = null,
        val issue: Issue,
        val comment: IssueComment,
    ) : GithubEventPayload
}

@Serializable
@SerialName("IssuesEvent")
data class IssuesEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<IssuesEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be one of `opened`, `edited`, `deleted`, `closed`, `reopened`, `assigned`, `unassigned`, `labeled`, `unlabeled`
        val assignee: User? = null,
        val changes: String? = null,
        val issue: Issue,
        val label: Label? = null,
    ) : GithubEventPayload
}

@Serializable
@SerialName("MemberEvent")
data class MemberEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<MemberEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be 'added'
        val member: User,
        val changes: String? = null,
    ) : GithubEventPayload
}

@Serializable
@SerialName("PublicEvent")
data class PublicEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<PublicEvent.Payload> {
    @Serializable
    data object Payload : GithubEventPayload
}

@Serializable
@SerialName("PullRequestEvent")
data class PullRequestEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<PullRequestEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be one of `opened`, `edited`, `closed`, `reopened`, `assigned`, `unassigned`, `review_requested`, `review_request_removed`, `labeled`, `unlabeled`, `synchronize`
        val number: Int,
        val changes: String? = null,
        @SerialName("pull_request")
        val pullRequest: PullRequest,
        val reason: String? = null,
    ) : GithubEventPayload
}

@Serializable
@SerialName("PullRequestReviewEvent")
data class PullRequestReviewEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<PullRequestReviewEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be `created`
        @SerialName("pull_request")
        val pullRequest: PullRequestSimple,
        val review: PRReview,
    ) : GithubEventPayload
}

@Serializable
@SerialName("PullRequestReviewCommentEvent")
data class PullRequestReviewCommentEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<PullRequestReviewCommentEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be `created`
        val changes: String? = null,
        @SerialName("pull_request")
        val pullRequest: PullRequestSimple,
        val comment: PRComment,
    ) : GithubEventPayload
}

// PullRequestReviewThreadEvent not used

@Serializable
@SerialName("PushEvent")
data class PushEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<PushEvent.Payload> {
    @Serializable
    data class Payload(
        @SerialName("repository_id")
        val repositoryId: Int? = null, // Not in docs
        @SerialName("push_id")
        val pushId: Long,
        val size: Int,
        @SerialName("distinct_size")
        val distinctSize: Int,
        val ref: String,
        val head: String,
        val before: String,
        val commits: List<Commit>,
    ) : GithubEventPayload
}

@Serializable
@SerialName("ReleaseEvent")
data class ReleaseEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<ReleaseEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be one of `published` (or `edited`?)
        val changes: String? = null,
        val release: Release,
    ) : GithubEventPayload
}

@Serializable
@SerialName("SponsorshipEvent")
data class SponsorshipEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<SponsorshipEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Can be `created`
        @SerialName("effective_date")
        val effectiveDate: String? = null,
        val changes: String? = null,
    ) : GithubEventPayload
}

@Serializable
@SerialName("WatchEvent")
data class WatchEvent(
    override val actor: Actor,
    @SerialName("created_at")
    override val createdAt: Instant,
    override val id: String,
    @SerialName("public")
    override val isPublic: Boolean,
    override val org: Org? = null,
    override val payload: Payload,
    override val repo: Repo,
    override val type: String,
) : GithubEvent<WatchEvent.Payload> {
    @Serializable
    data class Payload(
        val action: String, // Currently, can only be started.
    ) : GithubEventPayload
}

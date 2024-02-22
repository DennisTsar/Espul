package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommitComment(
    @SerialName("author_association")
    val authorAssociation: AuthorAssociation,
    val body: String,
    @SerialName("commit_id")
    val commitId: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    val line: Int?,
    @SerialName("node_id")
    val nodeId: String,
    val path: String?,
    val position: Int?,
    // optional in docs
    val reactions: Reactions,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
    // nullable in docs
    val user: User,
)

@Serializable
enum class AuthorAssociation {
    COLLABORATOR,
    CONTRIBUTOR,

    //    FIRST_TIMER,
//    FIRST_TIME_CONTRIBUTOR,
//    MANNEQUIN,
    MEMBER,
    NONE,
    OWNER,
}

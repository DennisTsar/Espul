package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Milestone(
    @SerialName("closed_at")
    val closedAt: Instant?,
    @SerialName("closed_issues")
    val closedIssues: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    // nullable in docs
    val creator: User,
    val description: String?,
    @SerialName("due_on")
    val dueOn: String?,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    @SerialName("labels_url")
    val labelsUrl: String,
    @SerialName("node_id")
    val nodeId: String,
    val number: Int,
    @SerialName("open_issues")
    val openIssues: Int,
    val state: OpenState,
    val title: String,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String,
)

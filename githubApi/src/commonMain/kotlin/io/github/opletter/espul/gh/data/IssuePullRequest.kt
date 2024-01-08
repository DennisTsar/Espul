package io.github.opletter.espul.gh.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssuePullRequest(
    // nullable in docs
    @SerialName("diff_url")
    val diffUrl: String,
    @SerialName("html_url")
    // nullable in docs
    val htmlUrl: String,
    // optional in docs
    @SerialName("merged_at")
    val mergedAt: Instant?,
    // nullable in docs
    @SerialName("patch_url")
    val patchUrl: String,
    // nullable in docs
    val url: String,
)

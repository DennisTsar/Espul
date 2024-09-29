package io.github.opletter.espul.gh.data

import kotlinx.serialization.Serializable

@Serializable
data class RepoRef(
    val label: String,
    val ref: String,
    val repo: RepoMin?,
    val sha: String,
    val user: User,
)

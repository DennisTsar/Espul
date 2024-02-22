package io.github.opletter.espul.gh.data

import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val id: Long,
    val name: String,
    val url: String,
)

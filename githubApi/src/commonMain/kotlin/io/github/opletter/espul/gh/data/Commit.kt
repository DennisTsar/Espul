package io.github.opletter.espul.gh.data

import kotlinx.serialization.Serializable

@Serializable
data class Commit(
    val author: Author,
    val distinct: Boolean,
    val message: String,
    val sha: String,
    val url: String,
)

@Serializable
data class Author(
    val email: String,
    val name: String,
)

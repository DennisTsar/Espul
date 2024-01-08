package io.github.opletter.espul.gh.data

import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    val admin: Boolean,
    val maintain: Boolean,
    val pull: Boolean,
    val push: Boolean,
    val triage: Boolean,
)

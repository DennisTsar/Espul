package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Org(
    val id: Int,
    val login: String,
    @SerialName("gravatar_id")
    val gravatarId: String,
    val url: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
)

package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("display_login")
    val displayLogin: String,
    @SerialName("gravatar_id")
    val gravatarId: String,
    val id: Int,
    val login: String,
    val url: String,
)

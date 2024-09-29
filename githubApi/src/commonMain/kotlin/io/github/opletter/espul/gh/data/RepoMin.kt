package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: investigate where these come from

@Serializable
data class RepoMin(
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    val private: Boolean,
)

@Serializable
data class RepoMin2(
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    val private: Boolean,
    @SerialName("public")
    val isPublic: Boolean,
)

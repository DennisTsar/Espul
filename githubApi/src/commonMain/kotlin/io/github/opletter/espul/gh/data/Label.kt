package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Label(
    val color: String,
    val default: Boolean,
    val description: String?,
    val id: Long,
    val name: String,
    @SerialName("node_id")
    val nodeId: String,
    val url: String,
)

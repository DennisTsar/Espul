package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class License(
    val key: String,
    val name: String,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("spdx_id")
    val spdxId: String,
    // not nullable in docs
    val url: String?,
)

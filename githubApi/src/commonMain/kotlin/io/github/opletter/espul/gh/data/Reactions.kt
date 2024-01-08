package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reactions(
    val confused: Int,
    val eyes: Int,
    val heart: Int,
    val hooray: Int,
    val laugh: Int,
    @SerialName("-1")
    val minusOne: Int,
    @SerialName("+1")
    val plusOne: Int,
    val rocket: Int,
    @SerialName("total_count")
    val totalCount: Int,
    val url: String,
)

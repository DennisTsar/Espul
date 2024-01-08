package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeOfConduct(
    @SerialName("html_url")
    val htmlUrl: String,
    val key: String,
    val name: String,
    val url: String,
)

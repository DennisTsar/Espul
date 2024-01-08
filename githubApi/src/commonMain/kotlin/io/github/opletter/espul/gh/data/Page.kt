package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val action: PageAction,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("page_name")
    val pageName: String,
    val sha: String,
    // not in docs
    val summary: String?,
    val title: String,
)

enum class PageAction {
    @SerialName("created")
    CREATED,

    @SerialName("edited")
    EDITED,
}

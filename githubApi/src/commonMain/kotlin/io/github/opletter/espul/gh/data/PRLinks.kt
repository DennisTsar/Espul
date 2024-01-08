package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PRLinks(
    val comments: UrlObject,
    val commits: UrlObject,
    val html: UrlObject,
    val issue: UrlObject,
    @SerialName("review_comment")
    val reviewComment: UrlObject,
    @SerialName("review_comments")
    val reviewComments: UrlObject,
    val self: UrlObject,
    val statuses: UrlObject,
)

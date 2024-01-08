package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutoMerge(
    @SerialName("commit_message")
    val commitMessage: String?,
    @SerialName("commit_title")
    val commitTitle: String?,
    @SerialName("enabled_by")
    val enabledBy: User,
    @SerialName("merge_method")
    val mergeMethod: MergeMethod,
)

enum class MergeMethod {
    @SerialName("squash")
    SQUASH,

    @SerialName("merge")
    MERGE,

    @SerialName("rebase")
    REBASE,
}

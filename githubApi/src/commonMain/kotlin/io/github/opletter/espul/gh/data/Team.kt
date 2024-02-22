package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNull

@Serializable
data class Team(
    val description: String?,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    @SerialName("members_url")
    val membersUrl: String,
    val name: String,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("notification_setting")
    val notificationSetting: String? = null,
    // TeamSimple but always null
    val parent: JsonNull,
    val permission: String,
//    val permissions: Permissions? = null,
    // optional in docs
    val privacy: String,
    @SerialName("repositories_url")
    val repositoriesUrl: String,
    val slug: String,
    val url: String,
)

//@Serializable
//data class TeamSimple(
//    val description: String?,
//    @SerialName("html_url")
//    val htmlUrl: String,
//    val id: Long,
//    @SerialName("ldap_dn")
//    val ldapDn: String? = null,
//    @SerialName("members_url")
//    val membersUrl: String,
//    val name: String,
//    @SerialName("node_id")
//    val nodeId: String,
//    @SerialName("notification_setting")
//    val notificationSetting: String? = null,
//    val permission: String,
//    val privacy: String? = null,
//    @SerialName("repositories_url")
//    val repositoriesUrl: String,
//    val slug: String,
//    val url: String,
//)

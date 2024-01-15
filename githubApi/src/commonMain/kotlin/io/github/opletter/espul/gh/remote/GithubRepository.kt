package io.github.opletter.espul.gh.remote

import io.github.opletter.espul.gh.data.Author
import io.github.opletter.espul.gh.data.GithubEvent
import io.github.opletter.espul.gh.data.GithubEventPayload
import io.github.opletter.espul.gh.data.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GithubRepository(private val apiKey: String?) {
    private val client = HttpClient {
        install(HttpCache)
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            apiKey?.let { bearerAuth(it) }
            header("X-GitHub-Api-Version", "2022-11-28")
            url {
                protocol = URLProtocol.HTTPS
                host = "api.github.com"
            }
        }
    }

    val isAuthenticated = apiKey != null

    suspend fun getUserEvents(username: String, page: Int = 1): List<GithubEvent<GithubEventPayload>> {
        val response = client.get("users/$username/events/public") {
            parameter("page", page)
        }
        response.headers.forEach { s, strings ->
            println("$s: $strings")
        }
        return response.body()
    }

    suspend fun getUser(username: String): User? {
        return try {
            client.get("users/$username").body()
        } catch (e: JsonConvertException) {
            null
        }
    }

    suspend fun getRepoFileContent(owner: String, repo: String, path: String): RepoContent? {
        return try {
            client.get("repos/$owner/$repo/contents/$path").body()
        } catch (e: JsonConvertException) {
            null
        }
    }

    suspend fun updateFileContent(
        owner: String,
        repo: String,
        path: String,
        message: String,
        content: String,
        committer: Author,
        sha: String? = null, // optional if file is null
    ): Boolean {
        return client.put("repos/$owner/$repo/contents/$path") {
            val payload = buildJsonObject {
                put("owner", owner)
                put("repo", repo)
                put("path", path)
                put("message", message)
                put("content", content.encodeBase64())
                put("committer", Json.decodeFromString<JsonObject>(Json.encodeToString(committer)))
                sha?.let { put("sha", it) }
            }
            setBody(Json.encodeToString(payload))
        }.status.isSuccess()
    }

    suspend fun overwriteFileContent(
        owner: String,
        repo: String,
        path: String,
        message: String,
        content: String,
        committer: Author,
    ): Boolean {
        val sha = getRepoFileContent(owner, repo, path)?.sha
        return updateFileContent(
            owner = owner,
            repo = repo,
            path = path,
            message = message,
            content = content,
            committer = committer,
            sha = sha
        )
    }
}

@Serializable
data class RepoContent(val content: String, val sha: String)

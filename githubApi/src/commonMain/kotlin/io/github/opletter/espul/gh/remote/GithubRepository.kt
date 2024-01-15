package io.github.opletter.espul.gh.remote

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
import kotlinx.serialization.json.Json

class GithubRepository(apiKey: String?) {
    private val client = HttpClient {
        install(HttpCache)
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            apiKey?.let { bearerAuth(it) }
            url {
                protocol = URLProtocol.HTTPS
                host = "api.github.com"
            }
        }
    }

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
}

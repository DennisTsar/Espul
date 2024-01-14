package io.github.opletter.espul.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.markdownTypography
import io.github.opletter.espul.gh.data.*
import io.github.opletter.espul.openUrl
import kotlinx.datetime.Instant
import nl.jacobras.humanreadable.HumanReadable

private data class EventCardDetails(
    val title: String,
    val url: String,
    val subhead: String? = null,
    val content: (@Composable ColumnScope.() -> Unit)? = null,
)

private data class EventCardData(
    val repoName: String,
    val timestamp: Instant,
    val details: EventCardDetails,
)

@Composable
fun EventCard(event: GithubEvent<*>) {
    val data = event.toEventCardData()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { openUrl(data.details.url) })
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row {
                Text(
                    text = data.repoName.uppercase(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = HumanReadable.timeAgo(data.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(
                text = data.details.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            if (data.details.subhead != null) {
                Markdown(
                    data.details.subhead,
                    typography = markdownTypography(paragraph = MaterialTheme.typography.labelLarge),
                )
            }

            if (data.details.content != null) {
                Column(
                    Modifier
                        .padding(top = 4.dp)
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState()),
                    content = data.details.content
                )
            }
        }
    }
}

@Composable
private fun SimpleMarkdown(text: String, modifier: Modifier = Modifier) {
    val filteredText = text.replace("\r\n", "\n").takeIf { it.isNotBlank() } ?: return
    Card(
        modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
    ) {
        Markdown(
            filteredText,
            modifier = Modifier.padding(6.dp),
            // match github's markdown style more closely
            typography = markdownTypography(
                h1 = MaterialTheme.typography.headlineLarge,
                h2 = MaterialTheme.typography.headlineSmall,
                h3 = MaterialTheme.typography.titleLarge,
                h4 = MaterialTheme.typography.titleMedium,
                h5 = MaterialTheme.typography.titleSmall,
                h6 = MaterialTheme.typography.bodyLarge,
            ),
        )
    }
}

private fun GithubEvent<*>.toEventCardData(): EventCardData {
    val baseRepoUrl = "https://github.com/${repo.name}"
    val details: EventCardDetails = when (this) {
        is CommitCommentEvent -> {
            EventCardDetails(
                title = "Commented on Commit",
                url = payload.comment.htmlUrl,
//                subhead =  // TODO: get the commit title from the commitId maybe?
                content = { SimpleMarkdown(payload.comment.body) },
            )
        }

        is CreateEvent -> {
            val url: String
            val text: String
            when (payload.refType) {
                CreateEvent.RefType.REPOSITORY -> {
                    url = baseRepoUrl
                    text = repo.name
                }

                else -> {
                    url = "$baseRepoUrl/tree/${payload.ref}"
                    text = payload.ref!!
                }
            }
            EventCardDetails(
                title = "Created ${payload.refType.name.lowercase().upperCaseFirst()}",
                url = url,
                subhead = text,
            )
        }

        is DeleteEvent -> {
            EventCardDetails(
                title = "Deleted ${payload.refType.name.lowercase().upperCaseFirst()}",
                url = baseRepoUrl,
            )
        }

        is ForkEvent -> {
            EventCardDetails(
                title = "Forked Repo",
                url = baseRepoUrl,
            )
        }

        is GollumEvent -> {
            EventCardDetails(
                title = "Updated Wiki",
                url = baseRepoUrl,
            ) {
                Column {
                    payload.pages.forEach { page ->
                        SimpleMarkdown(
                            page.title,
                            Modifier.clickable { openUrl(page.htmlUrl) }
                        )
                    }
                }
            }
        }

        is IssueCommentEvent -> {
            val obj = if (payload.issue.pullRequest != null) "PR" else "Issue"
            EventCardDetails(
                title = "Commented on $obj",
                url = payload.comment.htmlUrl,
                subhead = payload.issue.title,
                content = { SimpleMarkdown(payload.comment.body) },
            )
        }

        is IssuesEvent -> {
            val action = payload.action.upperCaseFirst()
            EventCardDetails(
                title = "$action Issue",
                url = payload.issue.htmlUrl,
                subhead = payload.issue.title,
                content = payload.issue.body?.let { { SimpleMarkdown(it) } },
            )
        }

        is MemberEvent -> {
            val action = payload.action.upperCaseFirst()
            EventCardDetails(
                title = "$action Member",
                url = payload.member.htmlUrl,
                subhead = payload.member.login,
            )
        }

        is PublicEvent -> {
            EventCardDetails(
                title = "Made Repo Public",
                url = baseRepoUrl,
            )
        }

        is PullRequestEvent -> {
            val action = payload.action.replace('_', ' ').upperCaseFirst()
                .let { if (it == "Closed" && payload.pullRequest.merged) "Merged" else it }
            EventCardDetails(
                title = "$action PR",
                url = payload.pullRequest.htmlUrl,
                subhead = payload.pullRequest.title,
                content = payload.pullRequest.body?.let {
                    {
                        // many PRs have auto-generated text below the divider
                        SimpleMarkdown(it.substringBefore("---\n"))
                    }
                },
            )
        }

        is PullRequestReviewCommentEvent -> {
            val action = payload.action.upperCaseFirst()
            EventCardDetails(
                title = "$action PR Comment",
                url = payload.comment.htmlUrl,
                subhead = payload.pullRequest.title,
                content = { SimpleMarkdown(payload.comment.body) },
            )
        }

        is PullRequestReviewEvent -> {
            EventCardDetails(
                title = "Reviewed PR",
                url = payload.review.htmlUrl,
                subhead = payload.pullRequest.title,
            ) {
                Text(payload.review.state.upperCaseFirst())
            }
        }

        is PushEvent -> {
            EventCardDetails(
                title = "Pushed ${payload.size} Commit${if (payload.size == 1) "" else "s"}",
                url = "$baseRepoUrl/commits/${payload.ref}",
                subhead = payload.ref.substringAfter("refs/heads/"),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    payload.commits.forEach { commit ->
                        val title = commit.message.substringBefore("\n")
                        val hasMore = commit.message.substringAfter("\n", missingDelimiterValue = "").isNotBlank()
                        SimpleMarkdown(
                            text = title + (if (hasMore) Typography.ellipsis else ""),
                            modifier = Modifier.clickable { openUrl("$baseRepoUrl/commit/${commit.sha}") }
                        )
                    }
                }
            }
        }

        is ReleaseEvent -> {
            val action = payload.action.upperCaseFirst()
            EventCardDetails(
                title = "$action Release",
                url = payload.release.htmlUrl,
                subhead = payload.release.name,
                content = payload.release.body?.let { { SimpleMarkdown(it) } },
            )
        }

        is SponsorshipEvent -> {
            val action = payload.action.upperCaseFirst()
            EventCardDetails(
                title = "$action Sponsorship",
                url = baseRepoUrl,
            )
        }

        is WatchEvent -> {
            EventCardDetails(
                title = "Starred a Repo",
                url = baseRepoUrl,
                subhead = repo.name,
            )
        }
    }
    return EventCardData(repo.name, createdAt, details)
}

private fun String.upperCaseFirst() = replaceFirstChar { it.uppercase() }

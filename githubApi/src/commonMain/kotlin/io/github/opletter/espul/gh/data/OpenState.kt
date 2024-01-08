package io.github.opletter.espul.gh.data

import kotlinx.serialization.SerialName

enum class OpenState {
    @SerialName("open")
    OPEN,

    @SerialName("closed")
    CLOSED,
}

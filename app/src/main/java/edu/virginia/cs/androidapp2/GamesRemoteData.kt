package edu.virginia.cs.androidapp2

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// I used the Mars Google Codelab as an example for how to do all of this: https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet
// I also used the retrofit docs: https://square.github.io/retrofit/
// I also used the docs for kotlinx.serialization Converter: https://github.com/square/retrofit/tree/trunk/retrofit-converters/kotlinx-serialization
// I also used the kotlinx.serialization docs: https://kotlinlang.org/docs/serialization.html#serialize-and-deserialize-json

@Serializable
data class GamesRemoteData (
    @SerialName(value = "games")
    val games: List<GameWrapperRemoteData> = listOf()
)

@Serializable
data class GameWrapperRemoteData(
    @SerialName(value = "game")
    val game: GameRemoteData,
)

@Serializable
data class GameRemoteData (
    @SerialName(value = "gameID")
    val id: String,

    @SerialName(value = "away")
    val away: TeamRemoteData,
    @SerialName(value = "home")
    val home: TeamRemoteData,

    @SerialName(value = "gameState")
    val gameState: String,

//    @SerialName(value = "startTime")
//    val startTime: String,
    @SerialName(value = "startTimeEpoch")
    val startTimeEpoch: String = "",

    @SerialName(value = "currentPeriod")
    val currentPeriod: String = "",
    @SerialName(value = "contestClock")
    val contestClock: String = "0:00",
)

@Serializable
data class TeamRemoteData (
    @SerialName(value = "names")
    val names: NameRemoteData,
    @SerialName(value = "score")
    val score: String = "",
    @SerialName(value = "winner")
    val winner: Boolean = false
)

@Serializable
data class NameRemoteData (
    @SerialName(value = "short")
    val short: String
)
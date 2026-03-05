package edu.virginia.cs.androidapp2;

import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class Game(
        @PrimaryKey val id: Long,

        val home: String,
        val away: String,

        val winner: String?,  // this will hold the value "home" or "away"

        @ColumnInfo(name = "home_score") val homeScore: Int?,
        @ColumnInfo(name = "away_score") val awayScore: Int?,

        @ColumnInfo(name = "game_state") val gameState: String,  // this will only hold the value "pre", "live", or "final"
        val period: String?,

        @ColumnInfo(name = "contest_clock") val contestClock: String,
        @ColumnInfo(name = "start_time") val startTime: Long,  // this is stored in epoch seconds

        val date: Long,  // this is stored in epoch ms
        val gender: String  // this will only be "men" or "women"
)
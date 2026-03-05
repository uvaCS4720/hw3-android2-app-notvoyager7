package edu.virginia.cs.androidapp2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class Game(
        @PrimaryKey val gameID: Long,

)
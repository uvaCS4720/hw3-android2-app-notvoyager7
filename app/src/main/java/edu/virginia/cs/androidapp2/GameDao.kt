package edu.virginia.cs.androidapp2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    // Gemini 3 Pro suggested adding this for batch inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)

    // pass the date in as epoch ms from the date picker
    @Query("SELECT * FROM Game WHERE date = :date and gender = :gender")
    fun getGames(date: Long, gender: String): Flow<List<Game>>
}
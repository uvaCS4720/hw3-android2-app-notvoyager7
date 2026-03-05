package edu.virginia.cs.androidapp2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Game::class], version = 1)
abstract class GameDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao

    // copied this singleton pattern from Professor McBurney's example in the Counters Lab
    companion object {
        private var instance: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, // the application context
                    GameDatabase::class.java, // the datatype of our database class
                    "game_database.db" // the SQLite filename
                ).build().also { instance = it }
            }
        }
    }
}
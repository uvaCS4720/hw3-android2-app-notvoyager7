package edu.virginia.cs.androidapp2

import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val gameDao: GameDao,
    private val gameAPI: GameAPIService
) {
    fun getGames(date: Long, gender: String): Flow<List<Game>> {
        return gameDao.getGames(date, gender)
    }

    suspend fun refreshGames(date: Long, gender: String) {

    }
}
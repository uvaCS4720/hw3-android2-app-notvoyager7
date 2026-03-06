package edu.virginia.cs.androidapp2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUIState(
    val date: Long?,
    val gender: String,
    val loading: Boolean,
    val error: Boolean
)

// Google Gemini 3 Pro helped me write, debug, and design this complex view model that pulls from multiple flows
class MainViewModel(
    val gameRepository: GameRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(
        MainUIState(
            date = DateTimeUtil.getCurrentEpochMs(),
            gender = "men",
            loading = false,
            error = false
        )
    )
    val uiState = _uiState.asStateFlow()

    // Needs a refresh when it starts
    init {
        refresh()
    }

    // This pattern was introduced to me by Gemini 3 Pro. It allows returning an inner flow based on an outer flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val games = uiState.flatMapLatest { state ->
        if (state.date != null) gameRepository.getGames(state.date, state.gender)
        else gameRepository.getGames(-1, state.gender)
    }.map { games ->
        games.sortedBy { it.startTime }
    }.stateIn(
        scope = viewModelScope,  // make the 'cold' flow 'hot' in the viewModelScope
        started = SharingStarted.WhileSubscribed(5000),  // only stop sharing if more than 5s pass
        initialValue = listOf()  // value to display before flow is first emitted
    )

    fun updateDate(date: Long?) {
        _uiState.update { uiState ->
            uiState.copy(date = date)
        }
    }

    fun updateGender(gender: String) {
        _uiState.update { uiState ->
            uiState.copy(gender = gender)
        }
    }

    fun refresh() {
        if (uiState.value.date == null) return

        _uiState.update { uiState ->
            uiState.copy(loading = true, error = false)
        }

        // Gemini 3 Pro suggested capturing these before in order to prevent possible race conditions
        val date = uiState.value.date!!
        val gender = uiState.value.gender

        viewModelScope.launch {
            // Gemini suggested this to fix a weird visual glitch when trying to refresh on airplane mode
            // The loading got set to false too fast to clear the animation
            // Force a minimum display time of 300ms so Compose registers 'loading = true'
            delay(300)

            val result = gameRepository.refreshGames(date, gender)
            // Gemini 3 suggested refactoring updated to uiState into one block (a good suggestion, so I took it)
            _uiState.update { uiState ->
                uiState.copy(
                    loading = false,
                    error = result == RefreshResult.ERROR
                )
            }
        }
    }
}

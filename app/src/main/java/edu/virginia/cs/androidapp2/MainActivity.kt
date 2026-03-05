package edu.virginia.cs.androidapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.virginia.cs.androidapp2.ui.theme.AndroidApp2Theme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    // copied this singleton pattern from Professor McBurney's example in the Counters Lab
    // this fetches the single gameDao for the entire application, and it only fetches it when it is needed
    // (that is what the 'lazy' part is for)
    private val gameDao by lazy {
        val database = GameDatabase.getDatabase(applicationContext)
        return@lazy database.gameDao()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var date: Long? by rememberSaveable { mutableStateOf(DateTimeUtil.getCurrentEpochMs()) }
            var gender: String by rememberSaveable { mutableStateOf("Men") }
            var loading: Boolean by rememberSaveable { mutableStateOf(false) }


            AndroidApp2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier
                        .padding(innerPadding)
                        .padding(start = 10.dp, end = 10.dp, top = 15.dp)) {
                        Text(
                            text = "Basketball Scores",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.displayMedium,  // Gemini 3 Pro showed me how to set the font this way to avoid issues with line height
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Row {
                            ModalDatePicker(
                                onDateSelected = { date = it },
                                text = DateTimeUtil.convertToLocalDateStringFromMS(date),
                                initialSelectedDateMillis = date,
                                disabled = loading
                            )

                            MinimalDropdownMenu(
                                options = listOf("Men", "Women"),
                                text = gender,
                                modifier = Modifier.padding(start = 5.dp),
                                onClick = { gender = it },
                                disabled = loading
                            )
                        }

                        // Got this component from official compose docs:
                        // https://developer.android.com/develop/ui/compose/components/pull-to-refresh
                        PullToRefreshBox(
                            isRefreshing = loading,
                            onRefresh = { loading = true },
                            modifier = Modifier
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 10.dp)) {
                                item {
                                    BasketballScoreCard(
                                        home = "Home",
                                        away = "Away",
                                        gameState = "pre",
                                        homeScore = null,
                                        awayScore = null,
                                        contestClock = "0:00",
                                        // TODO: make sure to check if startTime is null
                                        startTime = "6:00 PM ET",
                                        period = null,
                                        winner = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Got these official android date pickers here: https://developer.android.com/develop/ui/compose/components/datepickers
@Composable
fun ModalDatePicker(
    onDateSelected: (Long?) -> Unit,
    text: String = "Select Date",
    initialSelectedDateMillis: Long? = null,
    disabled: Boolean = false
) {
    val pickingDate = remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { pickingDate.value = !pickingDate.value },
        shape = RectangleShape,
        enabled = !disabled
    ) {
        Text(text = text)
    }

    if (pickingDate.value) {
        DatePickerModalInput(
            onDateSelected = { onDateSelected(it) },
            onDismiss = { pickingDate.value = !pickingDate.value },
            initialSelectedDateMillis = initialSelectedDateMillis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long?
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = initialSelectedDateMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// Took this component from the official Compose docs:
// https://developer.android.com/develop/ui/compose/components/menu
@Composable
fun MinimalDropdownMenu(
    modifier: Modifier = Modifier,
    options: List<String> = listOf(),
    text: String = "Open Dropdown",
    onClick: (option: String) -> Unit,
    disabled: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    Box (
        modifier = modifier
    ) {
        Button(
            onClick = {expanded = !expanded},
            shape = RectangleShape,
            enabled = !disabled
        ) {
            Text(text)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (option in options) {
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onClick(option)
                    }
                )
            }
        }
    }
}

// Basketball Score Card generated by Google Gemini 3 based on my specifications
// I modified this as I saw fit, modifying styling and logic that needed correcting
@Composable
fun BasketballScoreCard(
    // Goes in name area of score card (left column, left side)
    home: String,
    away: String,

    // Goes by team names based on who won
    winner: String? = null,  // "home" or "away"

    // Goes in score area of score card (left column, right side)
    homeScore: Int? = null,  // null when not "live"
    awayScore: Int? = null,  // null when not "live"

    // Goes in state area of score card (right column, top)
    gameState: String,  // "pre", "live", or "final"
    period: String? = null,  // displayed next to gameState when "live"

    // Goes in clock/start time area of score card (right column, bottom)
    contestClock: String,  // only show when "live"
    startTime: String,  // replaces contestClock when "pre"
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Min), // Allows VerticalDivider to match column height
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: Teams and Scores
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Away Team Row (Team 1)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val awayName = if (gameState == "final" && winner == "away") "$away (W)" else away
                    Text(text = awayName, fontWeight = FontWeight.Bold)

                    if (awayScore != null) {
                        Text(text = awayScore.toString(), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Home Team Row (Team 2)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val homeName = if (gameState == "final" && winner == "home") "$home (W)" else home
                    Text(text = homeName, fontWeight = FontWeight.Bold)

                    if (homeScore != null) {
                        Text(text = homeScore.toString(), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Separator
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Right Side: Game State and Time
            Column(
                modifier = Modifier.width(100.dp), // Keeps the right side aligned consistently
                verticalArrangement = Arrangement.Center
            ) {
                // State Text Logic
                val stateText = when (gameState.lowercase()) {
                    "pre" -> "Upcoming"
                    "live" -> if (period != null) "Live - $period" else "Live"
                    "final" -> "FINAL"
                    else -> ""
                }

                Text(
                    text = stateText,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Sub-text Logic
                val subText = when (gameState.lowercase()) {
                    "pre" -> startTime
                    "live" -> contestClock
                    else -> "" // "final" shows nothing
                }

                if (subText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
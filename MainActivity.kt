
package com.example.ca3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]

        setContent {
            MaterialTheme {
                WorkoutScreen(
                    state = viewModel.workoutState,
                    onStart = { viewModel.startWorkout() },
                    onPause = { viewModel.pauseWorkout() },
                    onResume = { viewModel.resumeWorkout() },
                    onComplete = { viewModel.completeWorkout() },
                    onAddRep = { viewModel.addRepetition() }
                )
            }
        }
    }
}

enum class WorkoutStatus {
    NOT_STARTED,
    IN_PROGRESS,
    PAUSED,
    COMPLETED
}

data class WorkoutState(
    val exercise: String = "Push-ups",
    val repetitions: Int = 0,
    val status: WorkoutStatus = WorkoutStatus.NOT_STARTED
)

class WorkoutViewModel : ViewModel() {

    var workoutState by mutableStateOf(WorkoutState())
        private set

    fun startWorkout() {
        workoutState = workoutState.copy(
            status = WorkoutStatus.IN_PROGRESS
        )
    }

    fun pauseWorkout() {
        workoutState = workoutState.copy(
            status = WorkoutStatus.PAUSED
        )
    }

    fun resumeWorkout() {
        workoutState = workoutState.copy(
            status = WorkoutStatus.IN_PROGRESS
        )
    }

    fun completeWorkout() {
        workoutState = workoutState.copy(
            status = WorkoutStatus.COMPLETED
        )
    }

    fun addRepetition() {
        if (workoutState.status == WorkoutStatus.IN_PROGRESS) {
            workoutState = workoutState.copy(
                repetitions = workoutState.repetitions + 1
            )
        }
    }
}

@Composable
fun WorkoutScreen(
    state: WorkoutState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onComplete: () -> Unit,
    onAddRep: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Workout Screen",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Current Exercise: ${state.exercise}"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Completed Repetitions: ${state.repetitions}"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Workout Status: ${state.status}"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onStart,
                enabled = state.status == WorkoutStatus.NOT_STARTED
            ) {
                Text("Start")
            }

            Button(
                onClick = onPause,
                enabled = state.status == WorkoutStatus.IN_PROGRESS
            ) {
                Text("Pause")
            }

            Button(
                onClick = onResume,
                enabled = state.status == WorkoutStatus.PAUSED
            ) {
                Text("Resume")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onAddRep,
                enabled = state.status == WorkoutStatus.IN_PROGRESS
            ) {
                Text("+ Rep")
            }

            Button(
                onClick = onComplete,
                enabled = state.status == WorkoutStatus.IN_PROGRESS ||
                        state.status == WorkoutStatus.PAUSED
            ) {
                Text("Complete")
            }
        }
    }
}

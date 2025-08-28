package com.example.quizapp.ui.screens.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.data.database.QuestionWithAnswers
import com.example.quizapp.ui.composables.AppBar

@Composable
fun PlayScreen(viewModel: PlayViewModel, quizId: Int, userId: Int, navController: NavController) {

    val quizData by viewModel.quizData.observeAsState()
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    var showResult by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    Scaffold(
        topBar = { AppBar(navController, title = "Play") },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            when {
                quizData == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading quiz...")
                    }
                }

                quizData?.questions.isNullOrEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No questions available for this quiz")
                    }
                }

                else -> {
                    val quizWithQuestions = quizData!!
                    val randomQuestions = remember(quizWithQuestions.questions) {
                        quizWithQuestions.getRandomQuestions(5)
                    }

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Column {
                                Text(
                                    text = quizWithQuestions.quiz.name,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = quizWithQuestions.quiz.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Answers: ${selectedAnswers.size}/${randomQuestions.size}",
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }

                        items(randomQuestions, key = { it.question.id }) { questionWithAnswers ->
                            QuestionItem(
                                questionWithAnswers = questionWithAnswers,
                                selectedAnswers = selectedAnswers,
                                showResult = showResult,
                                onAnswerSelected = { answerId ->
                                    if (!showResult) {
                                        selectedAnswers[questionWithAnswers.question.id] = answerId
                                    }
                                }
                            )
                        }

                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        showResult = true
                                        score = calculateScore(selectedAnswers, randomQuestions)
                                        viewModel.insertScore(userId, quizId, score)
                                        // Badge: Tutto corretto
                                        if (score == randomQuestions.size) {
                                            viewModel.assignBadge(
                                                userId,
                                                "Perfect Score",
                                                "You answered all the questions correctly!",
                                                iconUri = "android.resource://com.example.quizapp/drawable/ic_badge_star",
                                                scope,
                                                snackbarHostState
                                            )
                                        }
                                        // Badge: Completato (anche se non tutto giusto)
                                        viewModel.assignBadge(
                                            userId,
                                            "Quiz Finisher",
                                            "You have completed the quiz to the end!",
                                            iconUri = "android.resource://com.example.quizapp/drawable/ic_badge_finish",
                                            scope,
                                            snackbarHostState
                                        )
                                        // Badge: Punteggio minimo
                                        if (score == 0) {
                                            viewModel.assignBadge(
                                                userId,
                                                "Oops!",
                                                "Oops! You did not answer any question correctly!",
                                                iconUri = "android.resource://com.example.quizapp/drawable/ic_badge_fail",
                                                scope,
                                                snackbarHostState
                                            )
                                        }

                                        // Badge: 80% corretto
                                        if (score >= (randomQuestions.size * 0.8)) {
                                            viewModel.assignBadge(
                                                userId,
                                                "Great Job",
                                                "You have exceeded 80% of correct answers!",
                                                iconUri = "android.resource://com.example.quizapp/drawable/ic_badge_great",
                                                scope,
                                                snackbarHostState
                                            )
                                        }
                                    },
                                    enabled = !showResult && selectedAnswers.size == randomQuestions.size
                                ) {
                                    Text("Check Answers")
                                }

                                if (showResult) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Score: $score/${randomQuestions.size}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { navController.popBackStack() }
                                    ) {
                                        Text("Go Back")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionItem(
    questionWithAnswers: QuestionWithAnswers,
    selectedAnswers: Map<Int, Int>,
    showResult: Boolean,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = questionWithAnswers.question.questionText,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        questionWithAnswers.answers.forEach { answer ->
            val isSelected = selectedAnswers[questionWithAnswers.question.id] == answer.id

            val backgroundColor = if (showResult) {
                when {
                    answer.isCorrect -> Color.Green.copy(alpha = 0.2f)
                    isSelected && !answer.isCorrect -> Color.Red.copy(alpha = 0.2f)
                    else -> Color.Transparent
                }
            } else Color.Transparent

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 4.dp)
                    .clickable(
                        enabled = !showResult,
                        onClick = { onAnswerSelected(answer.id) }
                    )
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onAnswerSelected(answer.id) },
                    enabled = !showResult
                )
                Text(
                    text = answer.answerText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

private fun calculateScore(
    selectedAnswers: Map<Int, Int>,
    questions: List<QuestionWithAnswers>
): Int {
    return questions.count { questionWithAnswers ->
        val selectedAnswerId = selectedAnswers[questionWithAnswers.question.id]
        val correctAnswer = questionWithAnswers.answers.find { it.isCorrect }
        selectedAnswerId == correctAnswer?.id
    }
}


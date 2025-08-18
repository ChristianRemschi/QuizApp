package com.example.quizapp.ui.screens.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.quizapp.ui.composables.AppBar

//@Composable
//fun PlayScreen(quiz: Quiz, navController: NavController){
//
//}
@Composable
fun PlayScreen(viewModel: PlayViewModel, quizId: Int, userId: Int, navController: NavController) {
    val quizData by viewModel.quizData.observeAsState()
    // Stato per le risposte selezionate: questionId -> answerId
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    // Stato per attivare il controllo delle risposte
    var showResult by remember { mutableStateOf(false) }

    var score = 0

    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    Scaffold(
        topBar = { AppBar(navController, title = "Play") }
    ) { contentPadding ->
        quizData?.let { quizWithQuestions ->
            Column(modifier = Modifier.padding(contentPadding)) {
                Text(
                    text = quizWithQuestions.quiz.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(text = quizWithQuestions.quiz.description)

                Spacer(modifier = Modifier.height(16.dp))

                quizWithQuestions.questions.forEach { questionWithAnswers ->
                    Text(
                        text = questionWithAnswers.question.questionText,
                        style = MaterialTheme.typography.titleMedium
                    )

                    questionWithAnswers.answers.forEach { answer ->
                        val isSelected = selectedAnswers[questionWithAnswers.question.id] == answer.id

                        // Colorazione delle risposte se showResult = true
                        val backgroundColor = if (showResult) {
                            when {
                                answer.isCorrect -> Color.Green.copy(alpha = 0.3f)
                                isSelected && !answer.isCorrect -> Color.Red.copy(alpha = 0.3f)
                                else -> Color.Transparent
                            }
                        } else Color.Transparent

                        if (answer.isCorrect && isSelected) {
                            score += 1
                        }


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backgroundColor)
                                .padding(4.dp)
                                .clickable {
                                    if (!showResult) {
                                        selectedAnswers[questionWithAnswers.question.id] = answer.id
                                    }
                                }
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    if (!showResult) {
                                        selectedAnswers[questionWithAnswers.question.id] = answer.id
                                    }
                                }
                            )
                            Text(text = answer.answerText)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showResult = true; viewModel.insertScore(userId, quizId, score) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Controlla Risposte")
                }
            }
        }
    }
}


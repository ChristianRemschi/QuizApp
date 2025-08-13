package com.example.quizapp.ui.screens.play

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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.quizapp.data.database.Quiz
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.composables.AppBar

//@Composable
//fun PlayScreen(quiz: Quiz, navController: NavController){
//
//}
@Composable
fun PlayScreen(viewModel: PlayViewModel,  quizId: Int, navController: NavController) {
    val quizData by viewModel.quizData.observeAsState()

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = false, // TODO: gestisci stato selezione
                                onClick = { /* TODO: selezione risposta */ }
                            )
                            Text(text = answer.answerText)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}

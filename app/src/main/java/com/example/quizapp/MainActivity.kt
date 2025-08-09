package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizapp.ui.theme.DeepOrange
import com.example.quizapp.ui.theme.QuizAppTheme


private const val TAG = "MyActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding->
                    Surface(
                        color = DeepOrange,
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    ) {
                        ScrollableList()
                    }
                }
            }
        }
    }
}

@Composable
fun DynamicContent(items: List<String>) {
    Column {
        for (item in items) {
            Text(item)
        }
    }
}
@Composable
fun ScrollableList() {
    val elems = (0..100).map { "Elem $it" }
    LazyColumn {
        items(elems) {
            Text(it)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizAppTheme {
        DynamicContent(listOf("First", "Second", "Third"))
    }
}


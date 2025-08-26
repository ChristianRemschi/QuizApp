//package com.example.quizapp.ui.screens.stats
//
//// PieChartComponent.kt
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.patrykandpatrick.vico.compose.chart.pie.pieChart
//import com.patrykandpatrick.vico.compose.chart.Chart
//import com.patrykandpatrick.vico.core.entry.entryModelOf
//
//@Composable
//fun PieChartComponent(
//    data: List<Pair<String, Int>>,
//    modifier: Modifier = Modifier
//) {
//    val entries = data.map { (_, value) -> value.toFloat() }
//    val colors = listOf(Color.Green, Color.Yellow, Color.Red)
//
//    Chart(
//        chart = pieChart(
//            colors = colors,
//            startAngle = -90f,
//            sweepAngle = 360f
//        ),
//        model = entryModelOf(entries),
//        modifier = modifier.size(200.dp)
//    )
//}
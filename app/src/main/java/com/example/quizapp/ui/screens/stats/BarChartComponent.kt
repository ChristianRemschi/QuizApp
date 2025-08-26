package com.example.quizapp.ui.screens.stats

import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun BarChartComponent(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {

    val entries = data.mapIndexed { index, (_, value) ->
        com.patrykandpatrick.vico.core.entry.FloatEntry(
            x = index.toFloat(),
            y = value
        )
    }

    val model = entryModelOf(entries)

    val axisColor = MaterialTheme.colorScheme.secondary
    val gridColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.secondary

    // Crea i componenti per gli assi
    val axisLine = com.patrykandpatrick.vico.compose.component.lineComponent(
        color = axisColor,
        thickness = 2.dp
    )

    val gridLine = com.patrykandpatrick.vico.compose.component.lineComponent(
        color = gridColor,
        thickness = 1.dp
    )

    val text = com.patrykandpatrick.vico.compose.component.textComponent(
        color = textColor
    )

    Chart(
        chart = columnChart(
            columns = listOf(
                com.patrykandpatrick.vico.compose.component.lineComponent(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        ),
        model = model,
        modifier = modifier.height(200.dp),
        bottomAxis = bottomAxis(
            axis = axisLine,       // Colore asse orizzontale
            guideline = gridLine,  // Colore linee griglia verticali
            tick = axisLine,       // Colore ticks
            label = text,           // Colore testo labels
            valueFormatter = { value, _ ->
                data.getOrNull(value.toInt())?.first ?: ""
            }
        ),
        startAxis = startAxis(
            axis = axisLine,       // Colore asse verticale
            guideline = gridLine,  // Colore linee griglia orizzontali
            tick = axisLine,       // Colore ticks
            label = text           // Colore testo labels
        )
    )
}

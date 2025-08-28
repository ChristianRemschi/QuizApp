package com.example.quizapp.ui.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun BarChartComponent(
    data: List<Pair<String, Float>>,
    title: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        }

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
                axis = axisLine,
                guideline = gridLine,
                tick = axisLine,
                label = text,
                valueFormatter = { value, _ ->
                    data.getOrNull(value.toInt())?.first ?: ""
                }
            ),
            startAxis = startAxis(
                axis = axisLine,
                guideline = gridLine,
                tick = axisLine,
                label = text
            )
        )
    }
}

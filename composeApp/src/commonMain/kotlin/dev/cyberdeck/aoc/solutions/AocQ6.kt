package dev.cyberdeck.aoc.solutions

import adventofcode.composeapp.generated.resources.Res
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.cyberdeck.aoc.getPlatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AocQ6() = AocScaffold(problem = 6) { innerPadding, input ->
    var solution by remember { mutableStateOf("" to emptyList<String>()) }
    var part2 by remember { mutableStateOf(false) }
    val progressDelay = 50.milliseconds

    LaunchedEffect(part2) {
        withContext(getPlatform().background) {
            val lines = input.split("\n")

            val histo = Array(lines.first().length) { mutableMapOf<Char, Int>() }

            for (i in lines.indices) {
                val line = lines[i]
                println("processing $line")
                for (j in line.indices) {
                    val cc = histo[j][line[j]] ?: 0
                    histo[j][line[j]] = cc + 1
                }

                val entrySelector: (Map<Char, Int>) -> Map.Entry<Char, Int> = if (part2) {
                    { map -> map.minBy { it.value } }
                } else {
                    { map -> map.maxBy { it.value } }
                }

                val candidate = histo.map(entrySelector).map { it.key }.joinToString("")
                val upcoming = lines.drop(i + 1).take(10)

                solution = candidate to upcoming
                println("emit solution = $solution")
                delay(progressDelay)
            }
        }
    }

    val textHeightDp = with(LocalDensity.current) {
        48.dp.toPx().roundToInt()
    }

    val (current, ahead) = solution
    if (current.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Row {
                Checkbox(
                    checked = part2,
                    onCheckedChange = {
                        part2 = it
                    }
                )
                Text("part two")
            }

            Text(
                fontSize = 48.sp,
                text = current
            )

            val infiniteTransition = rememberInfiniteTransition(label = "slide")

            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(progressDelay.toInt(DurationUnit.MILLISECONDS), easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "x animation"
            )
            ahead.forEachIndexed { index, line ->
                Text(
                    text = line,
                    fontSize = 48.sp,
                    modifier = Modifier.offset {
                        IntOffset(0, (offset * -textHeightDp).toInt())
                    }.alpha(if(index == 0) 1.0f - offset else 1.0f)
                )
            }
        }
    }
}
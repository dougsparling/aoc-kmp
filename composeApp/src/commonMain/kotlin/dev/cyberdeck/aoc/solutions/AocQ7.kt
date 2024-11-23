package dev.cyberdeck.aoc.solutions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import dev.cyberdeck.aoc.getPlatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AocQ7() = AocScaffold(problem = 7) { innerPadding, input ->
    var solution by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    var part2 by remember { mutableStateOf(false) }
    val progressDelay = 50.milliseconds

    LaunchedEffect(part2) {
        withContext(getPlatform().background) {
            val lines = input.split("\n")
            solution = emptyList()
            val solver = if (part2) {
                solveQ7p2(lines)
            } else {
                solveQ7p1(lines)
            }
            solver.collect { next ->
                solution += next
                delay(progressDelay)
            }
        }
    }

    Column(
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

        val label = if (part2) {
            "support SSL"
        } else {
            "vulnerable to TLS"
        }

        Text(
            text = "${solution.size} $label",
            fontSize = 32.sp,
            maxLines = 1,
        )

        solution.reversed().take(10).forEach { (hit, line) ->
            Text(
                text = buildAnnotatedString {
                    val matchAt = line.indexOf(hit)
                    val start = line.slice(0 until matchAt)
                    val match = line.slice(matchAt until matchAt + hit.length)
                    val end = line.slice(matchAt + hit.length until line.length)

                    append(start)
                    withStyle(SpanStyle(color = Color.Red)) {
                        append(match)
                    }
                    append(end)
                },
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }
}

fun solveQ7p1(lines: List<String>): Flow<Pair<String, String>> = flow {
    lines.forEach { line ->
        val segments = line.split(']', '[')
        // after split, odd segments are inside hypernet
        val abbaSupernet = segments.flatMapIndexed { i, s ->
            if (i % 2 == 0) {
                listOfNotNull(findAbba(s))
            } else {
                emptyList()
            }
        }
        val abbaHypernet = segments.filterIndexed { i, s -> i % 2 == 1 && findAbba(s) != null }

        if (abbaSupernet.isNotEmpty() && abbaHypernet.isEmpty()) {
            emit(abbaSupernet.first() to line)
        }
    }
}

fun solveQ7p2(lines: List<String>): Flow<Pair<String, String>> = flow {
    lines.forEach { line ->
        val segments = line.split(']', '[')
        // after split, odd segments are inside hypernet
        val supernetAbas = segments.flatMapIndexed { i, s ->
            if (i % 2 == 0) {
                findAllAba(s)
            } else {
                emptyList()
            }
        }
        val supernetBabs = supernetAbas.map { aba ->
            "${aba[1]}${aba[0]}${aba[1]}"
        }

        val hypernetMatches = segments.filterIndexed { i, s -> i % 2 == 1 && supernetBabs.any { bab -> s.contains(bab) } }

        if (hypernetMatches.isNotEmpty()) {
            emit(supernetAbas.first() to line)
        }
    }
}

fun findAbba(segment: String): String? {
    for (charIndex in 0 until segment.length - 3) {
        if (segment[charIndex] == segment[charIndex + 3] &&
            segment[charIndex + 1] == segment[charIndex + 2] &&
            segment[charIndex] != segment[charIndex + 1]
        ) {
            return segment.slice(charIndex..charIndex + 3)
        }
    }
    return null
}

fun findAllAba(segment: String) = (0 until segment.length - 2).flatMap { charIndex ->
    if (segment[charIndex] == segment[charIndex + 2] && segment[charIndex] != segment[charIndex + 1]) {
        listOf(segment.slice(charIndex..charIndex + 2))
    } else {
        emptyList()
    }
}

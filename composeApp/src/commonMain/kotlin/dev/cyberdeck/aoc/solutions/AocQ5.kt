package dev.cyberdeck.aoc.solutions

import adventofcode.composeapp.generated.resources.Res
import adventofcode.composeapp.generated.resources.allStringResources
import adventofcode.composeapp.generated.resources.h4x0r
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AocQ5() {
    var entry by remember { mutableStateOf("") }
    var doorId by remember { mutableStateOf("") }
    var solution by remember { mutableStateOf("") }
    var log by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(doorId) {
        doorId.takeIf { it.isNotBlank() } ?: return@LaunchedEffect
        withContext(Dispatchers.Compute) {
            val length = 10
            startSolver(
                doorId,
                difficulty = 7,
                length = length
            ).collect { (pwd, hash) ->
                solution = pwd + Random.nextBytes(8).hex().take(length - pwd.length)
                log = log + hash
                if (log.size > 10) {
                    log = log.drop(1)
                }
            }
        }
    }

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = Color.Green,
        ),
        typography = Typography(defaultFontFamily = FontFamily.Monospace)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                    value = entry,
                    onValueChange = { entry = it },
                    label = { Text("Door ID") }
                )
                Button(
                    onClick = {
                        doorId = entry
                    }
                ) {
                    Text(text = stringResource(Res.string.h4x0r))
                }

                FlowRow {
                    solution.toCharArray().forEach { letter ->
                        Text(
                            modifier = Modifier.border(
                                border = BorderStroke(width = 1.dp, color = Color.Green)
                            ).padding(4.dp),
                            text = letter.toString(),
                            fontSize = 48.sp,
                        )
                    }
                }

                val formattedLog = buildAnnotatedString {
                    log.forEach { line ->
                        val zeroPrefix = line.takeWhile { it == '0' }
                        val remainder = line.drop(zeroPrefix.length)

                        withStyle(style = SpanStyle(color = Color.Red, background = Color.Black)) {
                            append(zeroPrefix)
                        }
                        append(remainder)
                        append("\n")
                    }
                }

                Text(
                    text = formattedLog
                )
            }
        }
    }
}

@Composable
@Preview
fun AocQ5Preview() {
    AocQ5()
}

fun startSolver(input: String, difficulty: Int, length: Int): Flow<Pair<String, String>> = flow {
    val prefix = "0".repeat(difficulty)
    var seq = 0
    var wip = ""
    while (wip.length < length) {
        val key = input + seq
        val hash = key.md5()
        if (hash.startsWith("00000")) {
            wip += hash[5]
            emit(wip to hash)
        } else if (seq % 100000 == 0) {
            emit(wip to hash)
        }
        seq++
    }
}

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
fun ByteArray.hex() = this.toUByteArray().toHexString()

expect fun String.md5(): String

expect val Dispatchers.Compute: CoroutineContext
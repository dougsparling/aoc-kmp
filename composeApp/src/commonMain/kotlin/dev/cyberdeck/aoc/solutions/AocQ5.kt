package dev.cyberdeck.aoc.solutions

import adventofcode.composeapp.generated.resources.Res
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
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.cyberdeck.aoc.getPlatform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AocQ5() {
    var difficulty by remember { mutableStateOf(5) }
    var length by remember { mutableStateOf(8) }

    AocScaffold(
        problem = 5,
        defaultTheme = Theme.L33T,
        extraActions = {
            var showingLengthDialog by remember { mutableStateOf(false) }
            IconButton(onClick = { showingLengthDialog = true }) {
                Text("🍆")
            }

            var showingDifficultyDialog by remember { mutableStateOf(false) }
            IconButton(onClick = { showingDifficultyDialog = true }) {
                Text("☠️")
            }

            if (showingLengthDialog) {
                UserInputDialog(
                    dialogTitle = "Password length (1-20)",
                    onInput = { str ->
                        str.toIntOrNull()?.takeIf { it in 1..20 }?.let {
                            length = it
                            showingLengthDialog = false
                        }
                    },
                    onDismissRequest = { showingLengthDialog = false }
                )
            }

            if (showingDifficultyDialog) {
                UserInputDialog(
                    dialogTitle = "Password cracking difficulty (1-15)",
                    onInput = { str ->
                        str.toIntOrNull()?.takeIf { it in 1..15 }?.let {
                            difficulty = it
                            showingDifficultyDialog = false
                        }
                    },
                    onDismissRequest = { showingDifficultyDialog = false }
                )
            }
        }
    ) { padding ->
        var entry by remember { mutableStateOf("") }
        var doorId by remember { mutableStateOf("") }
        var solution by remember { mutableStateOf("") }

        var log by remember { mutableStateOf(listOf<String>()) }

        LaunchedEffect(doorId, length, difficulty) {
            doorId.takeIf { it.isNotBlank() } ?: return@LaunchedEffect
            withContext(getPlatform().background) {
                startSolver(
                    doorId,
                    difficulty = difficulty,
                    length = length
                ).collect { (pwd, hash) ->
                    solution =
                        pwd + Random.nextBytes((length + 1) / 2).hex().take(length - pwd.length)
                    log = log + hash
                    if (log.size > 10) {
                        log = log.drop(1)
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                    Text(
                        color = LocalContentColor.current,
                        text = stringResource(Res.string.h4x0r)
                    )
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
        if (hash.startsWith(prefix)) {
            wip += hash[5]
            emit(wip to hash)
        } else if (seq % 100000 == 0) {
            emit(wip to hash)
        }
        seq++
    }
}

fun String.md5() = getPlatform().md5(this)

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
fun ByteArray.hex() = this.toUByteArray().toHexString()
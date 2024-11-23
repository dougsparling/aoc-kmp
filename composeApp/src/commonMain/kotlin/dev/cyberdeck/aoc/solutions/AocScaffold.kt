package dev.cyberdeck.aoc.solutions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import dev.cyberdeck.aoc.BuildKonfig
import dev.cyberdeck.aoc.LocalNav
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.core.use

enum class Theme { Default, L33T }

@Composable
fun AocScaffold(
    problem: Int,
    defaultTheme: Theme = Theme.Default,
    extraActions: @Composable RowScope.() -> Unit = {},
    content: @Composable (innerPadding: PaddingValues, input: String) -> Unit
) {
    val nav = LocalNav.current
    var theme by remember { mutableStateOf(defaultTheme) }
    var puzzleInput by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(problem) {
        val client = HttpClient {
            expectSuccess = true
        }
        client.use {
            val res = client.get("https://adventofcode.com/2016/day/$problem/input") {
                headers {
                    append(HttpHeaders.Cookie, "session=${BuildKonfig.AOC_COOKIE}")
                }
            }

            if (res.status == HttpStatusCode.OK) {
                puzzleInput = res.bodyAsBytes().decodeToString().trim()
            }
        }
    }

    ThemedContent(theme = theme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.primary,
                    title = {
                        Text("AoC Q$problem")
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier.clickable {
                                nav.navigateUp()
                            }
                        )
                    },
                    actions = {
                        extraActions()
                        ThemeSwitcher { theme = it }
                        ProblemDescription(problem = problem)
                    }
                )
            },
            content = { innerPadding ->
                val input = puzzleInput
                if (input != null) {
                    content(innerPadding, input)
                } else {
                    Box(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        )
    }
}

@Composable
private fun ThemedContent(
    theme: Theme,
    content: @Composable () -> Unit,
) {
    val movableContent = remember(content) {
        movableContentOf(content)
    }
    when (theme) {
        Theme.Default -> movableContent()
        Theme.L33T -> {
            MaterialTheme(
                colors = MaterialTheme.colors.copy(
                    primary = Color.Green,
                    primaryVariant = Color.Yellow,
                    onSecondary = Color.Yellow,
                    onSurface = Color.Red,
                    onPrimary = Color.Black,
                    secondary = Color.Black,
                    secondaryVariant = Color.DarkGray,
                    surface = Color.Black,
                    background = Color.Black,
                    onBackground = Color.White,
                    isLight = false
                ),
                typography = Typography(defaultFontFamily = FontFamily.Monospace)
            ) {
                movableContent()
            }
        }
    }
}

@Composable
private fun ThemeSwitcher(
    onChangeTheme: (Theme) -> Unit
) {
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = {
        isDropDownExpanded = true
    }) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "switch theme"
        )
    }

    DropdownMenu(
        expanded = isDropDownExpanded,
        onDismissRequest = {
            isDropDownExpanded = false
        }
    ) {
        Theme.entries.forEach { theme ->
            DropdownMenuItem(
                content = { Text(text = theme.name) },
                onClick = {
                    isDropDownExpanded = false
                    onChangeTheme(theme)
                }
            )
        }
    }
}

@Composable
private fun ProblemDescription(problem: Int) {
    val uriHandler = LocalUriHandler.current

    IconButton(onClick = {
        uriHandler.openUri("https://adventofcode.com/2016/day/$problem")
    }) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "get problem description"
        )
    }

}

@Composable
fun UserInputDialog(
    onDismissRequest: () -> Unit,
    onInput: (String) -> Unit,
    dialogTitle: String,
    dialogText: String = "",
) {
    var inputText by remember { mutableStateOf("") }
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            if (dialogText != "") {
                Text(text = dialogText)
            }
            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                }
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onInput(inputText)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}
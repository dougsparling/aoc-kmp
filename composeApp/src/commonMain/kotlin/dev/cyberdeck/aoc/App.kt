package dev.cyberdeck.aoc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.cyberdeck.aoc.solutions.AocQ5
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalNav = compositionLocalOf<NavController> { error("must provide NavController") }

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        CompositionLocalProvider(
            LocalNav provides navController
        ) {
            NavHost(navController = navController, startDestination = Home) {
                composable<Home> {
                    HomeScreen()
                }
                composable<AocQ5> {
                    AocQ5()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val nav = LocalNav.current
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { nav.navigate(AocQ5) }) {
            Text("Question 5")
        }
    }
}
package dev.cyberdeck.aoc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Advent of Code",
    ) {
        App()
    }
}
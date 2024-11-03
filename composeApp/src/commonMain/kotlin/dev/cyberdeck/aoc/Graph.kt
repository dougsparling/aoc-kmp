package dev.cyberdeck.aoc

import kotlinx.serialization.Serializable

interface Route

// Define a home route that doesn't take any arguments
@Serializable
object Home: Route

// Define a profile route that takes an ID
@Serializable
object AocQ5: Route
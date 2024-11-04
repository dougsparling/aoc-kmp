package dev.cyberdeck.aoc

import kotlinx.serialization.Serializable

interface Route

@Serializable
object Home: Route

@Serializable
object AocQ5: Route

@Serializable
object AocQ6: Route
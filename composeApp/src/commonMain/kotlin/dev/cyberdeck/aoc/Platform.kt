package dev.cyberdeck.aoc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
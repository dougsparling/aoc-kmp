package dev.cyberdeck.aoc

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "こんにちは, ${platform.name}!"
    }
}
package dev.cyberdeck.aoc

import kotlin.coroutines.CoroutineContext

interface Platform {
    val name: String
    val background: CoroutineContext
    fun md5(input: String): String
    val httpClientIsTrash: Boolean
}

expect fun getPlatform(): Platform
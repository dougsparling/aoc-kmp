package dev.cyberdeck.aoc.solutions

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@JsModule("crypto-js")
external fun MD5(input: String): String

actual fun String.md5() = MD5(this)

// TODO: web doesn't have threads, how could this ever work, lol
actual val Dispatchers.Compute: CoroutineContext get() = Default
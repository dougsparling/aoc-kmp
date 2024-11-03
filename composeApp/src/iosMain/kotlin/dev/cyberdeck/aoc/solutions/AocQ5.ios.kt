package dev.cyberdeck.aoc.solutions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.coroutines.CoroutineContext

lateinit var actualMd5Hex: (String) -> String

actual fun String.md5() = actualMd5Hex(this)
actual val Dispatchers.Compute: CoroutineContext get() = Dispatchers.IO
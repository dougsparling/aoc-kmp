package dev.cyberdeck.aoc.solutions

import kotlinx.coroutines.Dispatchers
import java.security.MessageDigest
import kotlin.coroutines.CoroutineContext

val md5 = MessageDigest.getInstance("MD5")

actual fun String.md5(): String = md5.digest(this.toByteArray()).hex()

actual val Dispatchers.Compute: CoroutineContext get() = Dispatchers.IO

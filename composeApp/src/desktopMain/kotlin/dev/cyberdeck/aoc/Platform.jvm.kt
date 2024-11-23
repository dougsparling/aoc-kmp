package dev.cyberdeck.aoc

import dev.cyberdeck.aoc.solutions.hex
import kotlinx.coroutines.asCoroutineDispatcher
import java.security.MessageDigest
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

private val md5 = MessageDigest.getInstance("MD5")
private val singleThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val background: CoroutineContext = singleThread
    override fun md5(input: String) = md5.digest(input.toByteArray()).hex()
    override val httpClientIsTrash: Boolean get() = false
}

actual fun getPlatform(): Platform = JVMPlatform()
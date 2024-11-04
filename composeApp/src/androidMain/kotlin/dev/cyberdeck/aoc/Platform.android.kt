package dev.cyberdeck.aoc

import android.os.Build
import dev.cyberdeck.aoc.solutions.hex
import kotlinx.coroutines.Dispatchers
import java.security.MessageDigest
import kotlin.coroutines.CoroutineContext

private val md5 = MessageDigest.getInstance("MD5") 

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val background: CoroutineContext get() = Dispatchers.IO
    override fun md5(input: String) = md5.digest(input.toByteArray()).hex()
}

actual fun getPlatform(): Platform = AndroidPlatform()
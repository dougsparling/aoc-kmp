package dev.cyberdeck.aoc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.UIKit.UIDevice
import kotlin.coroutines.CoroutineContext

lateinit var actualMd5Hex: (String) -> String

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun md5(input: String) = actualMd5Hex(input)
    override val background: CoroutineContext get() = Dispatchers.IO
    override val httpClientIsTrash: Boolean get() = false
}

actual fun getPlatform(): Platform = IOSPlatform()
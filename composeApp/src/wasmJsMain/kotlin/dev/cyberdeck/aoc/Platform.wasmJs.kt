package dev.cyberdeck.aoc

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@JsModule("crypto-js")
external fun MD5(input: String): String

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val background: CoroutineContext get() = Dispatchers.Main
    override fun md5(input: String) = MD5(input)
}

actual fun getPlatform(): Platform = WasmPlatform()
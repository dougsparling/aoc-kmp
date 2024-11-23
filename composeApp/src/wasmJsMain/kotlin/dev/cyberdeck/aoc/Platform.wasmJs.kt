package dev.cyberdeck.aoc

import kotlinx.coroutines.Dispatchers

@JsModule("crypto-js")
external fun MD5(input: String): String

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val background get() = Dispatchers.Main
    override fun md5(input: String) = MD5(input)
    override val httpClientIsTrash: Boolean get() = true
}

actual fun getPlatform(): Platform = WasmPlatform()
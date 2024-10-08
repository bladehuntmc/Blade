package net.bladehunt.blade.module

import net.bladehunt.blade.scope.BladeScope
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

class PropertiesModule : BladeModule {
    val properties: Properties = Properties()

    constructor(vararg properties: Pair<String, Any>) {
        properties.forEach { this.properties.setProperty(it.first, it.second.toString()) }
    }

    constructor(properties: Map<String, Any>) {
        properties.forEach { this.properties.setProperty(it.key, it.value.toString()) }
    }

    constructor(file: File) {
        check(file.exists()) { "File $file does not exist" }
        FileInputStream(file).use { stream -> properties.load(stream) }
    }

    constructor(inputStream: InputStream) {
        properties.load(inputStream)
    }

    override suspend fun onCreate() {
        properties.forEach { System.getProperties()[it.key] = it.value }
    }

    override suspend fun onInit(scope: BladeScope) {}

    override fun onShutdown() {}
}

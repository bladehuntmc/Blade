package net.bladehunt.blade.module

import io.github.cdimascio.dotenv.Dotenv
import java.net.InetSocketAddress
import net.bladehunt.blade.scope.BladeScope

class DotenvModule(
    val dotenv: Dotenv,
    /** Loads address to host the server on from .env */
    var loadAddress: Boolean = false
) : BladeModule {
    override suspend fun onCreate() {}

    override suspend fun onInit(scope: BladeScope) {
        if (loadAddress) {
            scope.host = InetSocketAddress(dotenv["HOSTNAME"], dotenv["PORT"].toInt())
        }
    }

    override fun onShutdown() {}
}

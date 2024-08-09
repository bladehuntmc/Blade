package net.bladehunt.blade

import java.net.InetSocketAddress
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.bladehunt.blade.module.BladeModule
import net.bladehunt.blade.scope.BladeScope
import net.bladehunt.kotstom.SchedulerManager
import net.bladehunt.kotstom.coroutines.startSuspending
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode

suspend inline fun blade(
    vararg modules: BladeModule,
    crossinline block: suspend BladeScope.() -> Unit
) = coroutineScope {
    modules.forEach { it.onCreate() }

    val scope =
        BladeScope(
            MinecraftServer.init(), InetSocketAddress("127.0.0.1", 25565), EventNode.all("blade"))

    scope.block()

    coroutineScope {
        modules.forEach {
            launch { it.onInit(scope) }
            SchedulerManager.buildShutdownTask(it::onShutdown)
        }
    }

    scope.server.startSuspending(scope.host)
}

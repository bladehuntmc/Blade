package net.bladehunt.blade.scope

import java.net.SocketAddress
import net.bladehunt.blade.ext.logger
import net.bladehunt.kotstom.SchedulerManager
import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

val BladeLogger = logger<BladeScope>()

data class BladeScope(
    val server: MinecraftServer,
    var host: SocketAddress,
    val eventNode: EventNode<Event>
) {
    val logger
        get() = BladeLogger

    fun onConfigure(block: AsyncPlayerConfigurationEvent.() -> Unit) {
        eventNode.listen<AsyncPlayerConfigurationEvent>(block)
    }

    fun onShutdown(runnable: Runnable) {
        SchedulerManager.buildShutdownTask(runnable)
    }
}

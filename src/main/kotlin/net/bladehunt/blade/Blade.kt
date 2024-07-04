package net.bladehunt.blade

import java.net.InetSocketAddress
import java.net.SocketAddress
import kotlinx.coroutines.runBlocking
import net.bladehunt.blade.module.BladeModule
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Blade {
    private val loadedModules: MutableList<BladeModule<*>> = arrayListOf()

    val eventNode: EventNode<Event> = EventNode.all("Blade").apply { priority = -1 }

    var address: SocketAddress = InetSocketAddress("127.0.0.1", 25565)

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun <T> install(module: BladeModule<T>) {
        module.configure {}
        module.install()
    }

    fun <T> install(module: BladeModule<T>, configure: T.() -> Unit) {
        module.configure(configure)
        module.install()
    }

    fun onShutdown(runnable: Runnable) {
        MinecraftServer.getSchedulerManager().buildShutdownTask(runnable)
    }

    inline fun blockOnShutdown(crossinline block: suspend () -> Unit) = onShutdown {
        runBlocking { block() }
    }

    fun configurePlayers(block: AsyncPlayerConfigurationEvent.() -> Unit) {
        eventNode.listen<AsyncPlayerConfigurationEvent>(block)
    }

    inline fun buildInstance(block: InstanceContainer.() -> Unit): InstanceContainer =
        InstanceManager.createInstanceContainer().apply(block)

    var AsyncPlayerConfigurationEvent.respawnPoint
        get() = this.player.respawnPoint
        set(value) {
            this.player.respawnPoint = value
        }
}

inline fun blade(block: Blade.() -> Unit) {
    val minecraftServer = MinecraftServer.init()
    Blade.block()

    minecraftServer.start(Blade.address)
}

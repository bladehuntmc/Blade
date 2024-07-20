package net.bladehunt.blade

import java.net.InetSocketAddress
import java.net.SocketAddress
import kotlinx.coroutines.runBlocking
import net.bladehunt.blade.ext.logger
import net.bladehunt.blade.module.BladeModule
import net.bladehunt.kotstom.GlobalEventHandler
import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

class Blade {
    companion object {
        val LOGGER = logger<Blade>()
    }

    private lateinit var server: MinecraftServer

    private val loadedModules: MutableList<BladeModule<*>> = arrayListOf()

    val eventNode: EventNode<Event> = EventNode.all("Blade").apply { priority = -1 }

    var address: SocketAddress = InetSocketAddress("127.0.0.1", 25565)

    fun <T> install(module: BladeModule<T>, configure: T.() -> Unit = {}) {
        module.configure(this, configure)
        module.install(this)

        loadedModules.add(module)
    }

    fun create(block: Blade.() -> Unit) {
        this.block()

        server.start(address)
    }

    fun init(): MinecraftServer {
        val server = MinecraftServer.init()
        GlobalEventHandler.addChild(eventNode)
        loadedModules.forEach { it.onInit(this) }

        this.server = server

        return server
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

    var AsyncPlayerConfigurationEvent.respawnPoint
        get() = this.player.respawnPoint
        set(value) {
            this.player.respawnPoint = value
        }
}

fun blade(block: Blade.() -> Unit) = Blade().apply { create(block) }

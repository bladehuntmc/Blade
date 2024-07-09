package net.bladehunt.blade.example

import net.bladehunt.blade.Blade.buildInstance
import net.bladehunt.blade.blade
import net.bladehunt.blade.example.command.JOIN_COMMAND
import net.bladehunt.blade.example.command.LOBBY_COMMAND
import net.bladehunt.blade.ext.modify
import net.bladehunt.blade.ext.polar
import net.bladehunt.blade.ext.withLighting
import net.bladehunt.blade.module.DotenvModule
import net.bladehunt.kotstom.CommandManager
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.register
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.block.Block

val LOBBY by lazy {
    buildInstance {
        withLighting()

        setGenerator { unit ->
            unit.modify {
                fillHeight(-64, -59, Block.BEDROCK)
                fillHeight(-59, 5, Block.STONE)
                fillHeight(5, 10, Block.DIRT)
                fillHeight(10, 11, Block.GRASS_BLOCK)
            }
        }

        polar {
            fromResource("lobby.polar")

            saveOnShutdown()

            preLoad(-5, -5, 5, 5)
        }

        eventNode().listen<PlayerSpawnEvent> { it.player.teleport(Pos(0.5, 11.0, 0.5)) }
    }
}

fun main() = blade {
    install(DotenvModule)

    CommandManager.register(JOIN_COMMAND, LOBBY_COMMAND)

    configurePlayers { spawningInstance = LOBBY }
}

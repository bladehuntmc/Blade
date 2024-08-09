package net.bladehunt.blade.example

import io.github.cdimascio.dotenv.dotenv
import net.bladehunt.blade.blade
import net.bladehunt.blade.dsl.instance.buildInstance
import net.bladehunt.blade.example.command.JoinCommand
import net.bladehunt.blade.example.command.LobbyCommand
import net.bladehunt.blade.module.DotenvModule
import net.bladehunt.kotstom.CommandManager
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.register
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.block.Block

val Lobby = buildInstance {
    enableLighting()

    generator { unit ->
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

    eventNode.listen<PlayerSpawnEvent> { it.player.teleport(Pos(0.5, 11.0, 0.5)) }
}

val dotenv = dotenv {
    systemProperties = true
    ignoreIfMissing = true
}

suspend fun main() =
    blade(DotenvModule(dotenv, loadAddress = true)) {
        CommandManager.register(JoinCommand, LobbyCommand)

        onConfigure { spawningInstance = Lobby }

        onShutdown { println("Shutting down") }
    }

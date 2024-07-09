package net.bladehunt.blade.example.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import net.bladehunt.blade.example.game.ParkourGame
import net.bladehunt.kotstom.dsl.kommand.buildSyntax
import net.bladehunt.kotstom.dsl.kommand.kommand

val JOIN_COMMAND = kommand {
    name = "join"

    buildSyntax {
        onlyPlayers()

        executorAsync(Dispatchers.Default) {
            coroutineScope {
                val game = ParkourGame.getOrCreateGame()
                game.addPlayer(player)
            }
        }
    }
}

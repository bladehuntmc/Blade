package net.bladehunt.blade.example.command

import kotlinx.coroutines.Dispatchers
import net.bladehunt.blade.example.game.ParkourGame
import net.bladehunt.kotstom.dsl.kommand.buildSyntax
import net.bladehunt.kotstom.dsl.kommand.kommand
import net.bladehunt.minigamelib.GameManager

val JoinCommand = kommand {
    name = "join"

    buildSyntax {
        onlyPlayers()

        executorAsync(Dispatchers.Default) {
            val game = GameManager.getOrCreateFirstJoinableGame(block = ::ParkourGame)
            game.addPlayer(player)
        }
    }
}

package net.bladehunt.blade.example.command

import net.bladehunt.blade.example.Lobby
import net.bladehunt.kotstom.dsl.kommand.buildSyntax
import net.bladehunt.kotstom.dsl.kommand.kommand

val LobbyCommand = kommand {
    name = "lobby"

    buildSyntax {
        onlyPlayers()

        executor { player.setInstance(Lobby) }
    }
}

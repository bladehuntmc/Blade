package net.bladehunt.blade.example.command

import net.bladehunt.blade.example.LOBBY
import net.bladehunt.kotstom.dsl.kommand.buildSyntax
import net.bladehunt.kotstom.dsl.kommand.kommand

val LOBBY_COMMAND = kommand {
    name = "lobby"

    buildSyntax {
        onlyPlayers()

        executor { player.setInstance(LOBBY) }
    }
}

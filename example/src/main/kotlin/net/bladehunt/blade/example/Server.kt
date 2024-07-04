package net.bladehunt.blade.example

import net.bladehunt.blade.blade
import net.bladehunt.blade.ext.modify
import net.bladehunt.blade.ext.withLighting
import net.bladehunt.blade.module.DotenvModule
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.block.Block

fun main() = blade {
    install(DotenvModule)

    val defaultInstance = buildInstance {
        withLighting()
        setGenerator { unit ->
            unit.modify {
                fillHeight(-64, -60, Block.BEDROCK)
                fillHeight(-59, 4, Block.STONE)
                fillHeight(5, 9, Block.DIRT)
                fillHeight(10, 10, Block.GRASS_BLOCK)
            }
        }
    }

    configurePlayers {
        spawningInstance = defaultInstance
        respawnPoint = Pos(0.5, 10.0, 0.5)
    }
}

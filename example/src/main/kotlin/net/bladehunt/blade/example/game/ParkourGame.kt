package net.bladehunt.blade.example.game

import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import net.bladehunt.blade.dsl.instance.buildInstance
import net.bladehunt.blade.example.Lobby
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.dsl.scheduleTask
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.kotstom.extension.adventure.text
import net.bladehunt.kotstom.extension.roundToBlock
import net.bladehunt.minigamelib.InstancedGame
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.bladehunt.minigamelib.element.countdown
import net.bladehunt.minigamelib.util.createElementInstanceEventNode
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.timer.TaskSchedule
import net.minestom.server.utils.NamespaceID

class ParkourGame : InstancedGame(UUID.randomUUID(), buildInstance { enableLighting() }) {
    override val id: NamespaceID = NamespaceID.from("blade_example", "parkour")

    private val requiredScore = 5

    override val descriptor: GameDescriptor = gameDescriptor {
        +element {
            val eventNode = createElementInstanceEventNode()
            eventNode.listen<PlayerMoveEvent> { it.isCancelled = true }
            eventNode.listen<PlayerSpawnEvent> { it.player.teleport(Pos(0.5, 10.0, 0.5, -90f, 0f)) }

            instance.setBlock(0, 9, 0, Block.BONE_BLOCK)

            repeat(requiredScore) { instance.setBlock(3 * it + 3, 9, 0, Block.GRASS_BLOCK) }

            countdown(2, 4, 5)

            sendMessage(text("Starting the parkour game!", NamedTextColor.YELLOW))
        }
        +element {
            val winner = CompletableDeferred<Player>()

            val task =
                instance.scheduler().scheduleTask(repeat = TaskSchedule.nextTick()) {
                    players.forEach {
                        val pos = it.position.sub(0.0, 0.1, 0.0).roundToBlock()

                        val block = floor(pos.x / 3.0).roundToInt()

                        if (block >= requiredScore && it.position.y > 9.9) {
                            winner.complete(it)
                        }
                    }
                }

            val result =
                withTimeoutOrNull(10000) {
                    val player = winner.await()
                    sendMessage(player.name + " has won the game!")
                    true
                }

            if (result != true) {
                sendMessage(text("Nobody won the game..."))
            }

            task.cancel()
        }

        +element {
            coroutineScope { players.map { async { it.setInstance(Lobby).await() } }.awaitAll() }
            InstanceManager.unregisterInstance(instance)
        }
    }

    override fun Player.sendToFallback() {
        setInstance(Lobby).join()
    }
}

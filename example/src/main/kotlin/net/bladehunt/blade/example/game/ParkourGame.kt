package net.bladehunt.blade.example.game

import kotlin.math.floor
import kotlin.math.roundToInt
import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import net.bladehunt.blade.dsl.buildInstance
import net.bladehunt.blade.example.LOBBY
import net.bladehunt.blade.ext.withLighting
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.dsl.runnable
import net.bladehunt.kotstom.extension.adventure.asComponent
import net.bladehunt.kotstom.extension.adventure.color
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.kotstom.extension.roundToBlock
import net.bladehunt.minigamelib.InstancedGame
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.bladehunt.minigamelib.element.countdown
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.timer.TaskSchedule

object ParkourGame : InstancedGame<ParkourGameInstance> {
    override val descriptor: GameDescriptor<ParkourGameInstance> = gameDescriptor {
        element {
            elementEventNode.apply {
                listen<PlayerMoveEvent> { it.isCancelled = true }
                listen<PlayerSpawnEvent> { it.player.teleport(Pos(0.5, 10.0, 0.5, -90f, 0f)) }
            }

            instance.setBlock(0, 9, 0, Block.BONE_BLOCK)

            repeat(requiredScore) { instance.setBlock(3 * it + 3, 9, 0, Block.GRASS_BLOCK) }

            countdown(2, 4, 5)

            currentGame = null

            sendMessage("Starting the parkour game!".color(NamedTextColor.YELLOW))
        }
        element {
            val winner = CompletableDeferred<Player>()

            val task = runnable {
                repeat = TaskSchedule.nextTick()

                run {
                    players.forEach {
                        val pos = it.position.sub(0.0, 0.1, 0.0).roundToBlock()

                        val block = floor(pos.x / 3.0).roundToInt()

                        if (block >= requiredScore && it.position.y > 9.9) {
                            winner.complete(it)
                            return@run
                        }
                    }
                }
            }

            task.schedule(instance.scheduler())

            val result =
                withTimeoutOrNull(10000) {
                    val player = winner.await()
                    sendMessage(player.name + " has won the game!")
                    true
                }

            if (result != true) {
                sendMessage("Nobody won the game...".asComponent())
            }

            task.cancel()
        }
        element {
            coroutineScope { players.map { async { it.setInstance(LOBBY).await() } }.awaitAll() }
            InstanceManager.unregisterInstance(instance)
        }
    }

    override fun getFallback(instance: ParkourGameInstance, player: Player): Instance = LOBBY

    var currentGame: ParkourGameInstance? = null

    suspend fun getOrCreateGame(): ParkourGameInstance {
        return currentGame
            ?: ParkourGameInstance(5, buildInstance { withLighting() }).apply {
                currentGame = this
                start(CoroutineScope(Dispatchers.Default))
            }
    }
}

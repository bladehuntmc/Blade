package net.bladehunt.blade.example.game

import kotlinx.coroutines.CoroutineScope
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.await
import net.bladehunt.minigamelib.instance.InstancedGameInstance
import net.bladehunt.minigamelib.store.Store
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.timer.TaskSchedule

class ParkourGameInstance(val requiredScore: Int, instance: Instance) :
    InstancedGameInstance<ParkourGameInstance>(instance, ParkourGame) {
    var Player.reachedBlocks by Store { mutableListOf<Int>() }

    // We override the start method here to await the lighting
    override suspend fun start(coroutineScope: CoroutineScope) {
        // Waits for lighting to populate the instance
        instance.await(TaskSchedule.nextTick())

        instance.eventNode().listen<PlayerSpawnEvent> { it.player.isAutoViewable = false }

        super.start(coroutineScope)
    }
}

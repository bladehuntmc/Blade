package net.bladehunt.blade.instance

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.math.max
import kotlin.math.min
import net.bladehunt.kotstom.SchedulerManager
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.InstanceContainer

abstract class ChunkLoaderBuilder<T : IChunkLoader>(private val instance: InstanceContainer) {
    abstract fun fromPath(path: Path)

    fun fromPath(path: String) = fromPath(Path(path))

    fun preLoad(startX: Int, startZ: Int, endX: Int, endZ: Int) {
        for (x in min(startX, endX)..max(startX, endX)) {
            for (z in min(startZ, endZ)..max(startZ, endZ)) {
                instance.loadChunk(x, z)
            }
        }
    }

    fun preLoad(x: Int, z: Int) {
        instance.loadChunk(x, z)
    }

    fun saveOnShutdown() {
        SchedulerManager.buildShutdownTask { instance.saveInstance() }
    }

    abstract fun build(): T
}

package net.bladehunt.blade.ext

import net.bladehunt.kotstom.InstanceManager
import net.minestom.server.ServerFlag
import net.minestom.server.instance.*
import net.minestom.server.utils.chunk.ChunkUtils
import java.util.concurrent.CompletableFuture

fun Instance.loadChunks(
    chunkX: Int,
    chunkZ: Int,
    range: Int = ServerFlag.CHUNK_VIEW_DISTANCE
): CompletableFuture<Void> {
    val futures = arrayListOf<CompletableFuture<Chunk>>()
    ChunkUtils.forChunksInRange(chunkX, chunkZ, range) { x, y -> futures.add(loadChunk(x, y)) }

    return CompletableFuture.allOf(*futures.toTypedArray())
}

fun Instance.relightChunks(chunkX: Int, chunkZ: Int, range: Int = ServerFlag.CHUNK_VIEW_DISTANCE) {
    val chunks = arrayListOf<Chunk>()
    ChunkUtils.forChunksInRange(chunkX, chunkZ, range) { x, y ->
        getChunk(x, y)?.let { chunk -> chunks.add(chunk) }
    }

    LightingChunk.relight(this, chunks)
}

fun InstanceContainer.newSharedInstance(): SharedInstance =
    InstanceManager.createSharedInstance(this)

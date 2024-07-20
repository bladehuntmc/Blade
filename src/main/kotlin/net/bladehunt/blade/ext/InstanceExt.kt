package net.bladehunt.blade.ext

import net.bladehunt.blade.Blade
import net.bladehunt.blade.instance.AnvilBuilder
import net.bladehunt.blade.instance.PolarBuilder
import net.bladehunt.kotstom.InstanceManager
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.SharedInstance
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.UnitModifier
import net.minestom.server.utils.chunk.ChunkSupplier

fun Instance.withLighting() {
    chunkSupplier = ChunkSupplier(::LightingChunk)
}

inline fun InstanceContainer.anvil(block: AnvilBuilder.() -> Unit) {
    chunkLoader = AnvilBuilder(this).apply(block).build()
}

inline fun InstanceContainer.polar(block: PolarBuilder.() -> Unit) {
    try {
        Class.forName("net.hollowcube.polar.PolarLoader")
    } catch (e: ClassNotFoundException) {
        Blade.LOGGER.error(
            "Can't load a Polar map without Polar (Fix: Add net.hollowcube:polar to your dependencies)")
        return
    }
    chunkLoader = PolarBuilder(this).apply(block).build()
}

fun InstanceContainer.newSharedInstance(): SharedInstance =
    InstanceManager.createSharedInstance(this)

inline fun GenerationUnit.modify(block: UnitModifier.() -> Unit) = this.modifier().apply(block)

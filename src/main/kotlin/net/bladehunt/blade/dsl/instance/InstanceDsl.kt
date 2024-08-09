package net.bladehunt.blade.dsl.instance

import net.bladehunt.blade.scope.BladeLogger
import net.bladehunt.kotstom.InstanceManager
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator
import net.minestom.server.instance.generator.UnitModifier
import net.minestom.server.timer.Scheduler
import net.minestom.server.utils.chunk.ChunkSupplier

@DslMarker @Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION) annotation class InstanceDsl

private val LightingChunkSupplier = ChunkSupplier(::LightingChunk)

@JvmInline
value class InstanceBuilder(val instance: InstanceContainer) {
    val eventNode
        get() = instance.eventNode()

    val scheduler: Scheduler
        get() = instance.scheduler()

    inline fun anvil(block: @InstanceDsl AnvilBuilder.() -> Unit) {
        instance.chunkLoader = AnvilBuilder(instance).apply(block).build()
    }

    inline fun polar(block: @InstanceDsl PolarBuilder.() -> Unit) {
        try {
            Class.forName("net.hollowcube.polar.PolarLoader")
        } catch (e: ClassNotFoundException) {
            BladeLogger.error(
                "Can't load a Polar map without Polar (Fix: Add net.hollowcube:polar to your dependencies)")
            return
        }
        instance.chunkLoader = PolarBuilder(instance).apply(block).build()
    }

    inline fun GenerationUnit.modify(block: UnitModifier.() -> Unit) = this.modifier().apply(block)

    fun generator(generator: Generator) {
        instance.setGenerator(generator)
    }

    fun enableLighting() {
        instance.chunkSupplier = LightingChunkSupplier
    }
}

inline fun buildInstance(block: InstanceBuilder.() -> Unit): InstanceContainer =
    InstanceBuilder(InstanceManager.createInstanceContainer()).apply(block).instance

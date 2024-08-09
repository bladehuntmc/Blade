package net.bladehunt.blade.dsl.instance

import java.nio.file.Path
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.anvil.AnvilLoader

class AnvilBuilder(instance: InstanceContainer) : ChunkLoaderBuilder<AnvilLoader>(instance) {
    private var loader: AnvilLoader? = null

    override fun fromPath(path: Path) {
        loader = AnvilLoader(path)
    }

    override fun build(): AnvilLoader = requireNotNull(loader)
}

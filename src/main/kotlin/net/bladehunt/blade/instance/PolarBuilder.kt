package net.bladehunt.blade.instance

import java.io.InputStream
import java.nio.file.Path
import net.hollowcube.polar.PolarLoader
import net.minestom.server.instance.InstanceContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PolarBuilder(instance: InstanceContainer) : ChunkLoaderBuilder<PolarLoader>(instance) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private var loader: PolarLoader? = null

    override fun fromPath(path: Path) {
        loader = PolarLoader(path)
    }

    fun fromStream(stream: InputStream) {
        loader = PolarLoader(stream)
    }

    inline fun <reified T> T.fromResource(name: String) {
        val resource = T::class.java.getResourceAsStream(name)

        if (resource == null) {
            logger.error("Failed to load resource from $name")
            return
        }

        fromStream(resource)
    }

    override fun build(): PolarLoader = requireNotNull(loader)
}

package net.bladehunt.blade.optional.polar

import net.hollowcube.polar.PolarLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.nio.file.Path

class PolarBuilder {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    var loader: PolarLoader? = null

    fun fromPath(path: Path) {
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

        loader = PolarLoader(resource)
    }

    fun build(): PolarLoader = requireNotNull(loader)
}

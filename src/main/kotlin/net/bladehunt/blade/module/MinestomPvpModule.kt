package net.bladehunt.blade.module

import io.github.togar2.pvp.PvpExtension
import net.bladehunt.blade.Blade
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MinestomPvpModule : BladeModule<Unit> {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun install(blade: Blade) {}

    override fun onInit(blade: Blade) {
        try {
            Class.forName("io.github.togar2.pvp.PvpExtension")
        } catch (_: ClassNotFoundException) {
            logger.error(
                "Can't install MinestomPvpModule - MinestomPvP is not in the classpath (Fix: Add it to dependencies)")
            return
        }

        PvpExtension.init()
    }

    override fun configure(blade: Blade, block: Unit.() -> Unit) {}
}

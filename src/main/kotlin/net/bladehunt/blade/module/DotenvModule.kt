package net.bladehunt.blade.module

import io.github.cdimascio.dotenv.Dotenv
import java.net.InetSocketAddress
import net.bladehunt.blade.Blade

object DotenvModule : BladeModule<DotenvModule.ModuleConfig> {
    class ModuleConfig {
        // Dotenv config

        var directory: String = "./"
        /** Sets the name of the .env. The default is .env */
        var filename: String = ".env"
        /** Do not throw an exception when .env is malformed */
        var ignoreIfMalformed = false
        /** Do not throw an exception when .env is missing */
        var ignoreIfMissing = true

        /**
         * Set env vars into System properties. Enables fetch them via e.g. System.getProperty(...)
         */
        var systemProperties = true

        /** Loads address to host the server on from .env */
        var loadAddress = false
    }

    private val config = ModuleConfig()

    lateinit var dotenv: Dotenv

    override fun install() {
        if (config.loadAddress)
            Blade.address = InetSocketAddress(dotenv["HOSTNAME"], dotenv["PORT"].toInt())
    }

    override fun configure(block: ModuleConfig.() -> Unit) {
        val config = config.apply(block)

        val dotenv = Dotenv.configure()
        dotenv.directory(config.directory)
        dotenv.filename(config.filename)
        if (config.ignoreIfMalformed) dotenv.ignoreIfMalformed()
        if (config.ignoreIfMissing) dotenv.ignoreIfMissing()
        if (config.systemProperties) dotenv.systemProperties()
        this.dotenv = dotenv.load()
    }
}

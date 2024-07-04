package net.bladehunt.blade.module

interface BladeModule<T> {
    fun install()

    fun configure(block: T.() -> Unit)
}

package net.bladehunt.blade.module

import net.bladehunt.blade.Blade

interface BladeModule<T> {
    fun install(blade: Blade)

    fun configure(blade: Blade, block: T.() -> Unit)

    fun onInit(blade: Blade) {}
}

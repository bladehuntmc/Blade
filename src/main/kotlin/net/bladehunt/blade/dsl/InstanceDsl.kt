package net.bladehunt.blade.dsl

import net.bladehunt.kotstom.InstanceManager
import net.minestom.server.instance.InstanceContainer

inline fun buildInstance(block: InstanceContainer.() -> Unit): InstanceContainer =
    InstanceManager.createInstanceContainer().apply(block)

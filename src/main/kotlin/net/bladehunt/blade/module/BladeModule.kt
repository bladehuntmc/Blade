package net.bladehunt.blade.module

import net.bladehunt.blade.scope.BladeScope

interface BladeModule {
    suspend fun onCreate()

    suspend fun onInit(scope: BladeScope)

    fun onShutdown()
}

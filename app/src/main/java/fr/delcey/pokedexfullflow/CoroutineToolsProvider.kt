package fr.delcey.pokedexfullflow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoroutineToolsProvider @Inject constructor() {
    val mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
    val ioCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    val sharingStartedStateInStrategy: SharingStarted = SharingStarted.WhileSubscribed(
        stopTimeoutMillis = 5_000,
        replayExpirationMillis = 0
    )
}
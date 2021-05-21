package fr.delcey.pokedexfullflow

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class CoroutineContextProvider @Inject constructor() {
    val main: CoroutineContext = Dispatchers.Main
    val io: CoroutineContext = Dispatchers.IO
}
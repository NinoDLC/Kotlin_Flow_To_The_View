# Kotlin_Flow_To_The_View
PoC using Flow to the View on an Android Projet. 

This is a better approach than using a `liveData{}` block or `viewModelScope` because 5 seconds after leaving the application (not killing it !), the coroutine is cancelled, avoid possibly unnecessary work. 
Also, this is pure Kotlin, so there's no need for LiveData dependencies. 

Alas, unit testing is a bit more complicated (need to inject both `Dispatchers` and `SharingStarted` strategy). 

See for yourself ! 

# Kotlin_Flow_To_The_View
PoC using Flow to the View on an Android Projet. 

TL;DR : 
|                          	|         Flow with `asLiveData()`        	|                      Flow with `stateIn()`                     	|         `viewModelScope.launch()` or `liveData {}`         	|
|--------------------------	|:---------------------------------------:	|:--------------------------------------------------------------:	|:-----------------------------------------------------------:	|
| Lifecycle                	|                   ✔️                    	|                               ✔️                               	|                             ❌                              	|
| Adjustable Timeout       	|                   ✔️                    	|                               ✔️                               	|                             ❌                              	|
| On timeout behavior      	| Restarts only if Flow didn't complete  	|        Always exposes initial state and restarts Flow           	|                             ❌                              	|
| Adjustable Initial state 	|                   ✔️                    	|                              ✔️✔️¹                               	|                             ✔️                              	|
| Dynamic Initial state 	|                   ✔️                    	|                               ❌²                               	|                             ✔️                              	|
| Unit Testing             	|     ✔️✔️ Must inject `Dispatchers`       	|    ✔️ Must inject `Dispatchers` and `SharingStarted` strategy   	|               ✔️✔️ Must inject `Dispatchers`                 	|
| Conciseness              	|                   ✔️                    	|                               ❌³                               	|                             ❌                              	|
| Complexity / Error prone 	|                   ✔️                    	|                               ❌                               	|                             ✔️                              	|
| LiveData dependency   	|                   ❌                    	|                               ✔️                               	|                             ❌                              	|

¹ Initial state is exposed automatically when coroutine is restarted  
² Initial state can't change between timeouts  
³ Need a `SharingStarted` strategy to be injected with the `Dispatcher`(s)  

Using `asLiveData()` or `stateIn()` is a better approach than using a `liveData {}` block or simply launching on the `viewModelScope` to publish to a `MutableLiveData` or `MutableStateFlow`. This is because when using `asLiveData()` or `stateIn()` the coroutine is cancelled 5 seconds after leaving the activity (not killing the application, just pressing 'home button' !), avoiding possibly unnecessary work. 

Also, there is one way to go with pure Kotlin, so there's no need for LiveData dependencies. Alas, unit testing is a bit more complicated (need to inject both `Dispatchers` and `SharingStarted` strategy). And, to my opinion, it's more error prone

See for yourself ! 
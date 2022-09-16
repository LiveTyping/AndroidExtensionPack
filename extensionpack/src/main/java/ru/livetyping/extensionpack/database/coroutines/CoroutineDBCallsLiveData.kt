package ru.livetyping.extensionpack.database.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import ru.livetyping.extensionpack.coroutines.*
import ru.livetyping.extensionpack.database.*

inline fun <T> ViewModel.makeDBCallLiveData(crossinline apiCall: suspend () -> T): LiveData<DBResult<T>> {
    return liveData(viewModelScope.coroutineContext) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }
}

inline fun <T> ViewModel.makeDBCallLiveData(
    mediatorLiveData: MediatorLiveData<DBResult<T>>,
    crossinline apiCall: suspend () -> T
) {
    val ld: LiveData<DBResult<T>> = liveData(viewModelScope.coroutineContext) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}

suspend fun <T> LiveDataScope<DBResult<T>>.subscribeDBCall(res: T) {
    emit(DBResult.Success(res))
}

inline fun <T> CoroutineScope.makeDBCallAsync(
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false,
    crossinline dbCall: suspend () -> T?
): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await())
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}

inline fun <T> ViewModel.makeDBCallAsync(
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false,
    crossinline dbCall: suspend () -> T?
): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await())
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCallAsync(
    queryModel: T?,
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()

            try {
                val task = async(ioDispatcher) {
                    queryModel
                }
                dbResult.subscribe(task.await())
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
    queryModel: T?,
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false
): Job {
    dbResult.queryingPost()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribePost(queryModel)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

fun <T> CoroutineScope.makeDBCallList(
    queryModel: T?,
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = true
): Job {
    dbResult.queryingPost()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribeListPost(queryModel, includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }

}

fun <T> ViewModel.makeDBCall(
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false,
    dbCall: suspend () -> T?
): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribePost(dbCall())
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

fun <T> ViewModel.makeDBCallList(
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = true,
    dbCall: suspend () -> T?
): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

fun ViewModel.makeDBCall(
    onCallExecuted: () -> Unit = {},
    onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
    dbCall: suspend () -> Unit
): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            withMainContext {
                onCallExecuted()
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false,
    dbCall: suspend () -> T?
): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribePost(dbCall())
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}

fun <T> CoroutineScope.makeDBCallList(
    dbResult: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = true,
    dbCall: suspend () -> T?
): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}

fun CoroutineScope.makeDBCall(
    onCallExecuted: () -> Unit = {},
    onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
    dbCall: suspend () -> Unit
): Job {
    return launch(ioDispatcher) {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
            launch(mainDispatcher) {
                onErrorAction(t)
            }
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}

fun <T> ViewModel.makeDBCall(
    onCallExecuted: () -> Unit = {},
    onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
    dbCall: suspend () -> T,
    onCalled: (model: T) -> Unit
): Job {
    return viewModelIOCoroutine {
        try {
            val call = dbCall()
            viewModelMainCoroutine {
                onCalled(call)
            }
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            withMainContext {
                onCallExecuted()
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
    onCallExecuted: () -> Unit = {},
    onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
    dbCall: suspend () -> T,
    onCalled: (model: T) -> Unit
): Job {
    return launch(ioDispatcher) {
        try {
            val call = dbCall()
            launch(mainDispatcher) {
                onCalled(call)
            }
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}
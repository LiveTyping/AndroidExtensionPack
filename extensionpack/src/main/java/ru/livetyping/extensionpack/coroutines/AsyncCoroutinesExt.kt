package ru.livetyping.extensionpack.coroutines

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

inline fun <T> AndroidViewModel.makeIOCall(
        crossinline onCallExecuted: () -> Unit = {},
        crossinline onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        crossinline ioCall: suspend () -> T,
        crossinline onCalled: (model: T) -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    ioCall()
                }
                onCalled(task.await())
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}

inline fun <T> CoroutineScope.makeIOCall(
        crossinline onCallExecuted: () -> Unit = {},
        crossinline onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        crossinline ioCall: suspend () -> T,
        crossinline onCalled: (model: T) -> Unit): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    ioCall()
                }
                onCalled(task.await())
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}
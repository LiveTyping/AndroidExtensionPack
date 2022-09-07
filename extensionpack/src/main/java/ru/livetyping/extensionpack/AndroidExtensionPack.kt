package ru.livetyping.extensionpack

import kotlinx.coroutines.*

fun CoroutineScope.launchUI(callback: suspend () -> Unit) = launch(Dispatchers.Main) { callback() }

fun CoroutineScope.launchUI(handler: CoroutineExceptionHandler, callback: suspend () -> Unit) =
    launch(Dispatchers.Main + handler) { callback() }

fun CoroutineScope.launchIO(callback: suspend () -> Unit) = launch(Dispatchers.IO) { callback() }

fun CoroutineScope.launchIO(handler: CoroutineExceptionHandler, callback: suspend () -> Unit) =
    launch(Dispatchers.IO + handler) { callback() }

suspend fun <T> withIO(callback: suspend () -> T) = withContext(Dispatchers.IO) { callback() }

suspend fun <T> withUI(callback: suspend () -> T) = withContext(Dispatchers.Main) { callback() }

fun <T> CoroutineScope.asyncIO(callback: suspend () -> T) = async(Dispatchers.IO) { callback() }
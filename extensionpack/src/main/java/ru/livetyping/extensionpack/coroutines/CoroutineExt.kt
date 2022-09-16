package ru.livetyping.extensionpack.coroutines

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

suspend inline fun <T, R> T.onMain(crossinline block: (T) -> R): R =
    withContext(mainDispatcher) { this@onMain.let(block) }

suspend inline fun <T> onMain(crossinline block: CoroutineScope.() -> T): T =
    withContext(mainDispatcher) { block.invoke(this@withContext) }


suspend inline fun <T, R> T.nonCancellable(crossinline block: (T) -> R): R =
    withContext(NonCancellable) { this@nonCancellable.let(block) }

suspend inline fun <T, R> T.onDefault(crossinline block: (T) -> R): R =
    withContext(defaultDispatcher) { this@onDefault.let(block) }

suspend inline fun <T, R> T.onIO(crossinline block: (T) -> R): R = withContext(ioDispatcher) { this@onIO.let(block) }
suspend inline fun <T> onIO(crossinline block: CoroutineScope.() -> T): T =
    withContext(ioDispatcher) { block.invoke(this@withContext) }

val mainDispatcher = Dispatchers.Main
val defaultDispatcher = Dispatchers.Default
val unconfinedDispatcher = Dispatchers.Unconfined
val ioDispatcher = Dispatchers.IO

fun <T> ioCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job =
    GlobalScope.launch(ioDispatcher, coroutineStart) {
        block()
    }

fun <T> mainCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job =
    GlobalScope.launch(mainDispatcher, coroutineStart) {
        block()
    }

fun <T> defaultCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job =
    GlobalScope.launch(defaultDispatcher, coroutineStart) {
        block()
    }

fun <T> unconfinedCoroutineGlobal(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend () -> T
): Job =
    GlobalScope.launch(unconfinedDispatcher, coroutineStart) {
        block()
    }

suspend fun <T> withMainContext(block: suspend () -> T): T =
    withContext(mainDispatcher) {
        block()
    }

suspend fun <T> withIOContext(block: suspend () -> T): T =
    withContext(ioDispatcher) {
        block()
    }

suspend fun <T> withDefaultContext(block: suspend () -> T): T =
    withContext(defaultDispatcher) {
        block()
    }


suspend fun <T> withUnconfinedContext(block: suspend () -> T): T =
    withContext(Dispatchers.Unconfined) {
        block()
    }

suspend fun <T> withNonCancellableContext(block: suspend () -> T): T =
    withContext(NonCancellable) {
        block()
    }

fun ViewModel.viewModelIOCoroutine(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend (scope: CoroutineScope) -> Unit = {}
): Job =
    viewModelScope.launch(ioDispatcher, coroutineStart) {
        action(this)
    }

fun ViewModel.viewModelMainCoroutine(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend (scope: CoroutineScope) -> Unit = {}
): Job =
    viewModelScope.launch(mainDispatcher, coroutineStart) {
        action(this)
    }

fun ViewModel.viewModelDefaultCoroutine(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend (scope: CoroutineScope) -> Unit = {}
): Job =
    viewModelScope.launch(defaultDispatcher, coroutineStart) {
        action(this)
    }

fun ViewModel.viewModelUnconfinedCoroutine(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend (scope: CoroutineScope) -> Unit = {}
): Job =
    viewModelScope.launch(unconfinedDispatcher, coroutineStart) {
        action(this)
    }

fun ViewModel.viewModelNonCancellableCoroutine(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    action: suspend (scope: CoroutineScope) -> Unit = {}
): Job =
    viewModelScope.launch(NonCancellable, coroutineStart) {
        action(this)
    }

inline fun CoroutineScope.main(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline function: suspend () -> Unit
) {
    launch(mainDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.io(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline function: suspend () -> Unit
): Job =
    launch(ioDispatcher, coroutineStart) {
        function()
    }

inline fun CoroutineScope.default(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline function: suspend () -> Unit
): Job =
    launch(defaultDispatcher, coroutineStart) {
        function()
    }

inline fun CoroutineScope.unconfined(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline function: suspend () -> Unit
): Job =
    launch(unconfinedDispatcher, coroutineStart) {
        function()
    }

inline fun CoroutineScope.nonCancellable(
    coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline function: suspend () -> Unit
): Job =
    launch(NonCancellable, coroutineStart) {
        function()
    }

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

fun AppCompatActivity.ioCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(ioDispatcher) {
        action(this)
    }


fun AppCompatActivity.mainCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(mainDispatcher) {
        action(this)
    }


fun AppCompatActivity.unconfinedCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(unconfinedDispatcher) {
        action(this)
    }


fun AppCompatActivity.defaultCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(defaultDispatcher) {
        action(this)
    }


fun AppCompatActivity.nonCancellableCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(NonCancellable) {
        action(this)
    }


fun Fragment.ioCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(ioDispatcher) {
        action(this)
    }


fun Fragment.mainCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(mainDispatcher) {
        action(this)
    }


fun Fragment.unconfinedCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(unconfinedDispatcher) {
        action(this)
    }


fun Fragment.defaultCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(defaultDispatcher) {
        action(this)
    }


fun Fragment.nonCancellableCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job =
    lifecycleScope.launch(NonCancellable) {
        action(this)
    }

suspend fun doParallel(vararg blocks: suspend () -> Any) = coroutineScope {
    blocks
        .map { async { it() } }
        .forEach { it.await() }
}

suspend fun <T> doParallelWithResult(vararg blocks: suspend () -> T) = coroutineScope {
    val result = mutableListOf<T>()
    blocks
        .map { async { it() } }
        .forEach { result.add(it.await()) }

    return@coroutineScope result
}
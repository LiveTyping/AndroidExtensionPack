package ru.livetyping.extensionpack.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()
val newThreadScheduler: Scheduler = Schedulers.newThread()
val ioThreadScheduler: Scheduler = Schedulers.io()

fun <T : Any> Observable<T>.runSafeOnMain(subscribeOn: Scheduler = newThreadScheduler): Observable<T> =
    observeOn(mainThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnComplete { unsubscribeOn(newThreadScheduler) }

fun <T : Any> Observable<T>.runSafeOnIO(subscribeOn: Scheduler = newThreadScheduler): Observable<T> =
    observeOn(ioThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnComplete { unsubscribeOn(newThreadScheduler) }

fun <T : Any> Flowable<T>.runSafeOnMain(subscribeOn: Scheduler = newThreadScheduler): Flowable<T> =
    observeOn(mainThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnComplete { unsubscribeOn(newThreadScheduler) }

fun <T : Any> Flowable<T>.runSafeOnIO(subscribeOn: Scheduler = newThreadScheduler): Flowable<T> =
    observeOn(ioThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnComplete { unsubscribeOn(newThreadScheduler) }

fun <T : Any> Single<T>.runSafeOnMain(subscribeOn: Scheduler = newThreadScheduler): Single<T> =
    observeOn(mainThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnSuccess { unsubscribeOn(newThreadScheduler) }

fun <T : Any> Single<T>.runSafeOnIO(subscribeOn: Scheduler = newThreadScheduler): Single<T> =
    observeOn(ioThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnSuccess { unsubscribeOn(newThreadScheduler) }

fun Completable.runSafeOnMain(subscribeOn: Scheduler = newThreadScheduler): Completable =
    observeOn(mainThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnComplete { unsubscribeOn(newThreadScheduler) }

fun Completable.runSafeOnIO(subscribeOn: Scheduler = newThreadScheduler): Completable =
    observeOn(ioThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnComplete { unsubscribeOn(newThreadScheduler) }

fun <T> Maybe<T>.runSafeOnMain(subscribeOn: Scheduler = newThreadScheduler): Maybe<T> =
    observeOn(mainThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnSuccess { unsubscribeOn(newThreadScheduler) }

fun <T> Maybe<T>.runSafeOnIO(subscribeOn: Scheduler = newThreadScheduler): Maybe<T> =
    observeOn(ioThreadScheduler)
        .subscribeOn(subscribeOn)
        .doOnError { unsubscribeOn(newThreadScheduler) }
        .doOnSuccess { unsubscribeOn(newThreadScheduler) }

fun Disposable?.unsubscribe() {
    this?.let {
        if (!isDisposed) {
            dispose()
        }
    }
}

fun rxTimer(
    oldTimer: Disposable?,
    time: Long,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    thread: Scheduler = Schedulers.computation(),
    observerThread: Scheduler = mainThreadScheduler, action: ((Long) -> Unit)
): Disposable? {
    oldTimer?.dispose()
    return Observable
        .timer(time, unit, thread)
        .observeOn(observerThread)
        .subscribe {
            action.invoke(it)
        }
}

fun Completable.ifCompletes(chainableCompletableInvocation: () -> Completable): Completable =
    Completable.create { emitter ->
        subscribe(
            {
                chainableCompletableInvocation().subscribe(
                    { emitter.ifNotDisposed { onComplete() } },
                    { error -> emitter.ifNotDisposed { onError(error) } }
                )
            },
            { error -> emitter.ifNotDisposed { onError(error) } }
        )
    }

inline fun CompletableEmitter.ifNotDisposed(body: CompletableEmitter.() -> Unit) {
    if (!isDisposed) body()
}

inline fun <reified T : Any> Observable<T>.applyNetworkSchedulers(): Observable<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Observable<T>.intervalRequest(duration: Long): Observable<T> {
    return this.throttleFirst(duration, TimeUnit.MILLISECONDS)
}

inline fun <reified T : Any> Flowable<T>.applyNetworkSchedulers(): Flowable<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Flowable<T>.intervalRequest(duration: Long): Flowable<T> {
    return this.throttleFirst(duration, TimeUnit.MILLISECONDS)
}

inline fun <reified T : Any> Single<T>.applyNetworkSchedulers(): Single<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T> Maybe<T>.applyNetworkSchedulers(): Maybe<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}
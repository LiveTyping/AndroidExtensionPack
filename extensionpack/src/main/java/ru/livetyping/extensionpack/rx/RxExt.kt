package ru.livetyping.extensionpack.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.processors.FlowableProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.TimeUnit

fun <T : Any> Observable<T>?.safe(factory: (() -> T)? = null) = this
    ?: factory?.let { Observable.just(it()) }
    ?: Observable.empty()

fun <T> Maybe<T>?.safe(factory: (() -> T)? = null) = this
    ?: factory?.let { Maybe.just(it()) }
    ?: Maybe.empty()


fun <T : Any> Flowable<T>?.safe(factory: (() -> T)? = null) = this
    ?: factory?.let { Flowable.just(it()) }
    ?: Flowable.empty()

fun <T> Completable.toSingle(item: () -> T) = andThen(Single.just(item()))

fun <T : Any> FlowableProcessor<T>.canPublish(): Boolean = !hasComplete() && !hasThrowable()

fun <T : Any> Observable<T>.joinToString(
    separator: String? = null,
    prefix: String? = null,
    postfix: String? = null
): Single<String> = collect(
    { StringBuilder(prefix ?: "") })
{ builder: StringBuilder, next: T ->
    builder.append(if (builder.length == (prefix?.length ?: 0)) "" else separator ?: "").append(next)
}
    .map { it.append(postfix ?: "").toString() }


fun <T : Any> Observable<T>.withIndex(): Observable<IndexedValue<T>> =
    zipWith(Observable.range(0, Int.MAX_VALUE), BiFunction { value, index -> IndexedValue(index, value) })


val mainThreadScheduler = AndroidSchedulers.mainThread()
val newThreadScheduler = Schedulers.newThread()
val ioThreadScheduler = Schedulers.io()
val computationScheduler = Schedulers.computation()
val trampolineScheduler = Schedulers.trampoline()

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

fun <T : Any> Observable<T>.asFlowable(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.LATEST)
        : Flowable<T> {
    return this.toFlowable(backpressureStrategy)
}

fun <T, R> Flowable<List<T>>.mapToList(mapper: (T) -> R): Flowable<List<R>> {
    return this.map { it.map { mapper(it) } }
}

fun <T, R> Observable<List<T>>.mapToList(mapper: (T) -> R): Observable<List<R>> {
    return this.map { it.map { mapper(it) } }
}

fun <T, R> Single<List<T>>.mapToList(mapper: ((T) -> R)): Single<List<R>> {
    return flatMap { Flowable.fromIterable(it).map(mapper).toList() }
}

fun <T : Any> Observable<T>.defer(): Observable<T> {
    return Observable.defer { this }
}

fun <T : Any> Single<T>.defer(): Single<T> {
    return Single.defer { this }
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

val Disposable?.isNullOrDisposed get() = this == null || isDisposed

val <T> Subject<T>.canPublish get() = !hasComplete() && !hasThrowable()

@Suppress("CheckResult")
fun <T : Any> Single<T>.subscribeIgnoringResult() {
    subscribe({}, {})
}

fun <T : Any> Single<T>.flatMapIf(predicate: Boolean, mapper: (T) -> Single<T>): Single<T> =
    if (predicate) flatMap { mapper(it) } else this

@Suppress("CheckResult")
fun Completable.subscribeIgnoringResult() {
    subscribe({}, {})
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

@Suppress("CheckResult")
fun <T : Any> Observable<T>.subscribeIgnoringResult() {
    subscribe({}, {})
}

inline fun <T : Any> Observable<T>.filterNotifications(crossinline predicate: (Notification<T>) -> Boolean): Observable<T> =
    materialize().filter { predicate(it) }.dematerialize { it }

inline fun <T : Any> MaybeEmitter<T>.ifNotDisposed(body: MaybeEmitter<T>.() -> Unit) {
    if (!isDisposed) body()
}

inline fun <T : Any> SingleEmitter<T>.ifNotDisposed(body: SingleEmitter<T>.() -> Unit) {
    if (!isDisposed) body()
}

inline fun CompletableEmitter.ifNotDisposed(body: CompletableEmitter.() -> Unit) {
    if (!isDisposed) body()
}

inline fun <T : Any> ObservableEmitter<T>.ifNotDisposed(body: ObservableEmitter<T>.() -> Unit) {
    if (!isDisposed) body()
}

fun Completable?.makeDBCall(
    compositeDisposable: CompositeDisposable,
    onThrow: (error: Throwable) -> Unit = { _ -> },
    onComplete: () -> Unit = {}
) {
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        onComplete()
    }, {
        onThrow.invoke(it)
    })?.addTo(compositeDisposable)
}

fun CompositeDisposable.makeDBCallCompletable(
    onComplete: () -> Unit = {},
    onThrow: (error: Throwable) -> Unit = { _ -> },
    function: () -> Completable?
) {
    function()?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            onComplete()
        }, {
            onThrow.invoke(it)
        })?.addTo(this@makeDBCallCompletable)
}


fun <T, R> Maybe<T>.mapSelf(mapper: T.() -> R) = map { it.mapper() }

fun <T> Subject<T>.canPublish(): Boolean = !hasComplete() && !hasThrowable()

inline fun <reified T : Any> Observable<T>.applyNetworkSchedulers(): Observable<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Observable<T>.applyComputationSchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.computation()).observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Observable<T>.intervalRequest(duration: Long): Observable<T> {
    return this.throttleFirst(duration, TimeUnit.MILLISECONDS)
}

inline fun <reified T : Any> Flowable<T>.applyNetworkSchedulers(): Flowable<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Flowable<T>.applyComputationSchedulers(): Flowable<T> {
    return this.subscribeOn(Schedulers.computation()).observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Flowable<T>.intervalRequest(duration: Long): Flowable<T> {
    return this.throttleFirst(duration, TimeUnit.MILLISECONDS)
}

inline fun <reified T : Any> Single<T>.applyNetworkSchedulers(): Single<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T : Any> Single<T>.applyComputationSchedulers(): Single<T> {
    return this.subscribeOn(Schedulers.computation()).observeOn(mainThreadScheduler)
}

inline fun <reified T> Maybe<T>.applyNetworkSchedulers(): Maybe<T> {
    return this.subscribeOn(ioThreadScheduler)
        .observeOn(mainThreadScheduler)
}

inline fun <reified T> Maybe<T>.applyComputationSchedulers(): Maybe<T> {
    return this.subscribeOn(Schedulers.computation()).observeOn(mainThreadScheduler)
}

fun CompositeDisposable.clearAndDispose() {
    clear()
    dispose()
}
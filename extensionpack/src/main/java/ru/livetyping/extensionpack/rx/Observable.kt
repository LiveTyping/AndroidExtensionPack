package ru.livetyping.extensionpack.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3

fun <T : Any> observable(block: (ObservableEmitter<T>) -> Unit): Observable<T> = Observable.create(block)

fun <T : Any> deferredObservable(block: () -> Observable<T>): Observable<T> = Observable.defer(block)

fun <T : Any> emptyObservable(): Observable<T> = Observable.empty()

fun <T : Any> observableOf(item: T): Observable<T> = Observable.just(item)
fun <T : Any> observableOf(vararg items: T): Observable<T> = Observable.fromIterable(items.toList())
fun <T : Any> observableOf(items: Iterable<T>): Observable<T> = Observable.fromIterable(items)

@JvmName("observableOfArray")
fun <T : Any> observableOf(items: Array<T>): Observable<T> = Observable.fromArray(*items)

fun observableOf(items: BooleanArray): Observable<Boolean> =
    Observable.fromArray(*items.toTypedArray())

fun observableOf(items: ByteArray): Observable<Byte> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: CharArray): Observable<Char> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: DoubleArray): Observable<Double> =
    Observable.fromArray(*items.toTypedArray())

fun observableOf(items: FloatArray): Observable<Float> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: IntArray): Observable<Int> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: LongArray): Observable<Long> = Observable.fromArray(*items.toTypedArray())
fun observableOf(items: ShortArray): Observable<Short> = Observable.fromArray(*items.toTypedArray())

fun <T : Any> observableFrom(block: () -> T): Observable<T> = Observable.fromCallable(block)

fun <T : Any> T.toObservable(): Observable<T> = Observable.just(this)
fun <T : Any> Throwable.toObservable(): Observable<T> = Observable.error(this)
fun <T : Any> (() -> Throwable).toObservable(): Observable<T> = Observable.error(this)

fun <First : Any, Second : Any, Res : Any> Observable<First>.zipWith(
    second: Observable<Second>,
    zipFun: (First, Second) -> Res
): Observable<Res> =
    zipWith(second, BiFunction { t1, t2 -> zipFun(t1, t2) })

fun <First : Any, Second : Any> Observable<First>.zipWith(second: Observable<Second>): Observable<Pair<First, Second>> =
    zipWith(second) { t1, t2 -> Pair(t1, t2) }

fun <T : Any, R> Observable<T>.mapSelf(mapper: T.() -> R) = map { it.mapper() }


fun <First : Any, Second : Any> zip(
    first: Observable<First>,
    second: Observable<Second>
): Observable<Pair<First, Second>> =
    first.zipWith(second)

fun <First : Any, Second : Any, Res : Any> zip(
    first: Observable<First>,
    second: Observable<Second>,
    zipFun: (First, Second) -> Res
): Observable<Res> =
    first.zipWith(second, zipFun)

fun <First : Any, Second : Any, Third : Any> zip(
    first: Observable<First>,
    second: Observable<Second>,
    third: Observable<Third>
): Observable<Triple<First, Second, Third>> = zip(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> zip(
    first: Observable<First>,
    second: Observable<Second>,
    third: Observable<Third>,
    zipFun: (First, Second, Third) -> Res
): Observable<Res> = Observable.zip(first, second, third, Function3(zipFun))

fun <First : Any, Second : Any, Res : Any> combine(
    first: Observable<First>,
    second: Observable<Second>,
    combFunc: (First, Second) -> Res
): Observable<Res> = Observable.combineLatest(first, second, BiFunction(combFunc))

fun <First : Any, Second : Any> combine(
    first: Observable<First>,
    second: Observable<Second>
): Observable<Pair<First, Second>> = combine(first, second) { t1, t2 -> Pair(t1, t2) }

fun <First : Any, Second : Any, Third : Any> combine(
    first: Observable<First>,
    second: Observable<Second>,
    third: Observable<Third>
): Observable<Triple<First, Second, Third>> =
    combine(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> combine(
    first: Observable<First>,
    second: Observable<Second>,
    third: Observable<Third>,
    combFunc: (First, Second, Third) -> Res
): Observable<Res> = Observable.combineLatest(first, second, third, Function3(combFunc))
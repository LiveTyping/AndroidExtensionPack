package ru.livetyping.extensionpack.rx

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3

fun <T : Any> emptyFlowable(): Flowable<T> = Flowable.empty()

fun <T : Any> flowableOf(item: T): Flowable<T> = Flowable.just(item)
fun <T : Any> flowableOf(vararg items: T): Flowable<T> = Flowable.fromIterable(items.toList())
fun <T : Any> flowableOf(items: Iterable<T>): Flowable<T> = Flowable.fromIterable(items)

@JvmName("flowableOfArray")
fun <T : Any> flowableOf(items: Array<T>): Flowable<T> = Flowable.fromArray(*items)

fun flowableOf(items: BooleanArray): Flowable<Boolean> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: ByteArray): Flowable<Byte> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: CharArray): Flowable<Char> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: DoubleArray): Flowable<Double> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: FloatArray): Flowable<Float> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: IntArray): Flowable<Int> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: LongArray): Flowable<Long> = Flowable.fromArray(*items.toTypedArray())
fun flowableOf(items: ShortArray): Flowable<Short> = Flowable.fromArray(*items.toTypedArray())

fun <T : Any> flowableFrom(block: () -> T): Flowable<T> = Flowable.fromCallable(block)

fun <T : Any> T.toFlowable(): Flowable<T> = Flowable.just(this)
fun <T : Any> Throwable.toFlowable(): Flowable<T> = Flowable.error(this)
fun <T : Any> (() -> Throwable).toFlowable(): Flowable<T> = Flowable.error(this)

fun <First : Any, Second : Any, Res : Any> Flowable<First>.zipWith(
    second: Flowable<Second>,
    zipFun: (First, Second) -> Res
): Flowable<Res> =
    zipWith(second, BiFunction { t1, t2 -> zipFun(t1, t2) })

fun <First : Any, Second : Any> Flowable<First>.zipWith(second: Flowable<Second>): Flowable<Pair<First, Second>> =
    zipWith(second) { t1, t2 -> Pair(t1, t2) }

fun <T : Any, R> Flowable<T>.mapSelf(mapper: T.() -> R) = map { it.mapper() }

fun <First : Any, Second : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    zipFun: (First, Second) -> Res
): Flowable<Res> =
    first.zipWith(second, zipFun)

fun <First : Any, Second : Any, Third : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>
): Flowable<Triple<First, Second, Third>> = zip(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> zip(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    zipFun: (First, Second, Third) -> Res
): Flowable<Res> = Flowable.zip(first, second, third, Function3(zipFun))

fun <First : Any, Second : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    combFunc: (First, Second) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, BiFunction(combFunc))

fun <First : Any, Second : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>
): Flowable<Pair<First, Second>> = combine(first, second) { t1, t2 -> Pair(t1, t2) }

fun <First : Any, Second : Any, Third : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>
): Flowable<Triple<First, Second, Third>> =
    combine(first, second, third) { t1, t2, t3 -> Triple(t1, t2, t3) }

fun <First : Any, Second : Any, Third : Any, Res : Any> combine(
    first: Flowable<First>,
    second: Flowable<Second>,
    third: Flowable<Third>,
    combFunc: (First, Second, Third) -> Res
): Flowable<Res> = Flowable.combineLatest(first, second, third, Function3(combFunc))
package ru.livetyping.extensionpack

fun <T> MutableList<T>.addOrReplace(item: T, predicate: (T) -> Boolean): Boolean {
    return this.indexOfFirst { predicate.invoke(it) }
        .takeIf { it >= 0 }
        ?.let { this[it] = item }
        ?.let { true }
        ?: this.add(item)
            .let { false }
}

fun <S : MutableList<T>, T> S.addAnd(index: Int, item: T): S {
    add(index, item)
    return this
}

fun <S : MutableCollection<T>, T> S.addAnd(item: T): S {
    add(item)
    return this
}

inline fun <reified T> Collection<T>.removeDuplicates(): MutableList<T> {
    return this.toSet().toMutableList()
}

infix fun <E> MutableList<E>.addIfNotExist(obj: E) = if (!this.contains(obj)) add(obj) else false

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

fun <T> MutableList<T>.swapAsList(index1: Int, index2: Int): MutableList<T> {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}
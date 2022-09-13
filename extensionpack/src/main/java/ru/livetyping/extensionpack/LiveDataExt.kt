package ru.livetyping.extensionpack

import android.util.Patterns
import androidx.lifecycle.MutableLiveData

fun MutableLiveData<String>.isValid() = this.value.isNullOrEmpty().not()

fun MutableLiveData<String>.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this.value!!).matches()

fun <T> MutableLiveData<MutableList<T>>.postAdd(item: T?) {
    item?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@postAdd.value ?: mutableListOf())
            add(it)
        }
        postValue(updatedItems)
    }
}

fun <T> MutableLiveData<MutableList<T>>.add(item: T?) {
    item?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@add.value ?: mutableListOf())
            add(it)
        }
        this.value = updatedItems
    }
}

fun <T> MutableLiveData<MutableList<T>>.postAdd(item: T?, position: Int) {
    item?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@postAdd.value ?: mutableListOf())
            add(position, it)
        }
        postValue(updatedItems)
    }
}

fun <T> MutableLiveData<MutableList<T>>.add(item: T?, position: Int) {
    item?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@add.value ?: mutableListOf())
            add(position, it)
        }
        this.value = updatedItems
    }
}

fun <T> MutableLiveData<MutableList<T>>.postAdd(items: List<T>?) {
    items?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@postAdd.value ?: mutableListOf())
            addAll(items)
        }
        postValue(updatedItems)
    }
}

fun <T> MutableLiveData<MutableList<T>>.add(items: List<T>?) {
    items?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@add.value ?: mutableListOf())
            addAll(items)
        }
        this.value = updatedItems
    }
}

fun <T> MutableLiveData<MutableList<T>>.replace(items: List<T>?) {
    items?.let {
        val newItems = mutableListOf<T>().also { it.addAll(items) }
        this.value = newItems
    }
}

fun <T> MutableLiveData<List<T>>.addList(items: List<T>?) {
    items?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@addList.value ?: mutableListOf())
            addAll(items)
        }
        this.value = updatedItems.distinct()
    }
}

fun <T> MutableLiveData<MutableList<T>>.postReplace(replacedItem: T, item: T) {
    val replacedItemIndex = value?.indexOf(value?.find { it == replacedItem })
    if (replacedItemIndex != null && replacedItemIndex != -1) {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@postReplace.value ?: mutableListOf())
            set(replacedItemIndex, item)
        }
        postValue(updatedItems)
    }
}

fun <T> MutableLiveData<MutableList<T>>.replace(replacedItem: T, item: T) {
    val replacedItemIndex = value?.indexOf(value?.find { it == replacedItem })
    if (replacedItemIndex != null && replacedItemIndex != -1) {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@replace.value ?: mutableListOf())
            set(indexOf(find { it == replacedItem }), item)
        }
        this.value = updatedItems
    }
}

fun <T> MutableLiveData<MutableList<T>>.postRemove(item: T?) {
    item?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@postRemove.value ?: mutableListOf())
            remove(it)
        }
        postValue(updatedItems)
    }
}

fun <T> MutableLiveData<MutableList<T>>.remove(item: T?) {
    item?.let {
        val updatedItems = mutableListOf<T>().apply {
            addAll(this@remove.value ?: mutableListOf())
            remove(it)
        }
        this.value = updatedItems
    }
}

fun <T> MutableLiveData<MutableList<T>>.removeIf(predicate: (T) -> Boolean) {
    val updatedItems = value?.filter { !predicate.invoke(it) }?.toMutableList()
    value = updatedItems
}

fun <T> MutableLiveData<MutableList<T>>.postRemoveIf(predicate: (T) -> Boolean) {
    val updatedItems = value?.filter { !predicate.invoke(it) }?.toMutableList()
    value = updatedItems
}

fun <T> MutableLiveData<T>.postClear() {
    postValue(null)
}

fun <T> MutableLiveData<T>.clear() {
    this.value = null
}

fun <T> MutableLiveData<List<T>>.clearList() {
    this.value = emptyList()
}

fun <T> MutableLiveData<MutableList<T>>.clearMutableList() {
    this.value = mutableListOf()
}

fun <T> MutableLiveData<T>.postNotify() {
    postValue(this.value)
}

fun <T> MutableLiveData<T>.notify() {
    if (this.value != null) this.value = this.value
}

class LiveDataWithSingleCallback<T>(
    private val callback: (T) -> Unit
) : MutableLiveData<T>() {
    var isDelivered = false
    override fun setValue(value: T) {
        if (isDelivered.not()) callback.invoke(value)
        super.setValue(value)
    }
}
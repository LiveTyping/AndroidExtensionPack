package com.crazylegend.database.rx

import androidx.lifecycle.MutableLiveData
import ru.livetyping.extensionpack.rx.ioThreadScheduler
import ru.livetyping.extensionpack.rx.mainThreadScheduler
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import ru.livetyping.extensionpack.database.*

fun <T> Flowable<T>?.makeDBCallPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    dropBackPressure: Boolean = false,
    includeEmptyData: Boolean = false
) {
    result.queryingPost()
    this?.let { call ->
        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                result.subscribePost(it)
            }, {
                result.callErrorPost(it)
            }).addTo(compositeDisposable)
    }
}

fun <T> Flowable<T>?.makeDBCallListPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    dropBackPressure: Boolean = false,
    includeEmptyData: Boolean = true
) {
    result.queryingPost()
    this?.let { call ->
        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            }).addTo(compositeDisposable)
    }
}

fun <T> Single<T>?.makeDBCallPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = false
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)
}

fun <T> Single<T>?.makeDBCallListPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = true
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T> Observable<T>?.makeDBCallPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = false
) {
    result.queryingPost()

    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribePost(it)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)
}

fun <T> Observable<T>?.makeDBCallListPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = true
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)

}

fun <T> Maybe<T>?.makeDBCallPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = false
) {
    result.queryingPost()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)
}

fun <T> Maybe<T>?.makeDBCallListPost(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = true
) {
    result.queryingPost()

    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribeListPost(it, includeEmptyData)
    }, {
        result.callErrorPost(it)
    })?.addTo(compositeDisposable)
}

fun <T> CompositeDisposable.makeDBCallPost(
    result: MutableLiveData<DBResult<T>>, dropBackPressure: Boolean = false,
    includeEmptyData: Boolean = false, function: () -> Flowable<T>?
) {
    result.queryingPost()
    val disposable = function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)

    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
            ?.subscribe({
                result.subscribePost(it)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
    } else {
        disposable
            ?.subscribe({
                result.subscribePost(it)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
    }
}

fun <T> CompositeDisposable.makeDBCallListPost(
    result: MutableLiveData<DBResult<T>>, dropBackPressure: Boolean = false,
    includeEmptyData: Boolean = false, function: () -> Flowable<T>?
) {
    result.queryingPost()
    val disposable = function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)

    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
    } else {
        disposable
            ?.subscribe({
                result.subscribeListPost(it, includeEmptyData)
            }, {
                result.callErrorPost(it)
            })
            ?.addTo(this)
    }
}

fun <T> CompositeDisposable.makeDBCallSinglePost(
    result: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false, function: () -> Single<T>?
) {
    result.queryingPost()
    function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            result.subscribePost(it)
        }, {
            result.callErrorPost(it)
        })
        ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallListSinglePost(
    result: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = true, function: () -> Single<T>?
) {
    result.queryingPost()
    function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            result.subscribeListPost(it, includeEmptyData)
        }, {
            result.callErrorPost(it)
        })
        ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallMaybePost(
    result: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false, function: () -> Maybe<T>?
) {
    result.queryingPost()
    function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            result.subscribeListPost(it, includeEmptyData)
        }, {
            result.callErrorPost(it)
        })
        ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallListMaybePost(
    result: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = true, function: () -> Maybe<T>?
) {
    result.queryingPost()
    function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            result.subscribeListPost(it, includeEmptyData)
        }, {
            result.callErrorPost(it)
        })
        ?.addTo(this)
}
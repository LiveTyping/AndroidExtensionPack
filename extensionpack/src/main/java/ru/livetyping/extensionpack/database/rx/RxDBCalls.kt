package ru.livetyping.extensionpack.database.rx

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import ru.livetyping.extensionpack.database.DBResult
import ru.livetyping.extensionpack.database.callError
import ru.livetyping.extensionpack.database.querying
import ru.livetyping.extensionpack.database.subscribe
import ru.livetyping.extensionpack.rx.ioThreadScheduler
import ru.livetyping.extensionpack.rx.mainThreadScheduler

fun <T : Any> Flowable<T>?.makeDBCall(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    dropBackPressure: Boolean = false,
    includeEmptyData: Boolean = false
) {
    result.querying()
    this?.let { call ->
        if (dropBackPressure) {
            call.onBackpressureDrop()
        }
        call.subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                result.subscribe(it)
            }, {
                result.callError(it)
            }).addTo(compositeDisposable)
    }
}

fun <T : Any> Single<T>?.makeDBCall(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = false
) {
    result.querying()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribe(it)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)
}

fun <T : Any> Observable<T>?.makeDBCall(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = false
) {
    result.querying()

    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribe(it)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)

}

fun <T> Maybe<T>?.makeDBCall(
    result: MutableLiveData<DBResult<T>>,
    compositeDisposable: CompositeDisposable,
    includeEmptyData: Boolean = false
) {
    result.querying()
    this?.subscribeOn(ioThreadScheduler)?.observeOn(mainThreadScheduler)?.subscribe({
        result.subscribe(it)
    }, {
        result.callError(it)
    })?.addTo(compositeDisposable)
}

fun <T> CompositeDisposable.makeDBCall(
    result: MutableLiveData<DBResult<T>>, dropBackPressure: Boolean = false,
    includeEmptyData: Boolean = false, function: () -> Flowable<T>?
) {
    result.querying()
    val disposable = function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)

    if (dropBackPressure) {
        disposable?.onBackpressureDrop()
            ?.subscribe({
                result.subscribe(it)
            }, {
                result.callError(it)
            })
            ?.addTo(this)
    } else {
        disposable
            ?.subscribe({
                result.subscribe(it)
            }, {
                result.callError(it)
            })
            ?.addTo(this)
    }
}

fun <T> CompositeDisposable.makeDBCallSingle(
    result: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false, function: () -> Single<T>?
) {
    result.querying()
    function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            result.subscribe(it)
        }, {
            result.callError(it)
        })
        ?.addTo(this)
}

fun <T> CompositeDisposable.makeDBCallMaybe(
    result: MutableLiveData<DBResult<T>>,
    includeEmptyData: Boolean = false, function: () -> Maybe<T>?
) {
    result.querying()
    function()
        ?.subscribeOn(ioThreadScheduler)
        ?.observeOn(mainThreadScheduler)
        ?.subscribe({
            result.subscribe(it)
        }, {
            result.callError(it)
        })
        ?.addTo(this)
}
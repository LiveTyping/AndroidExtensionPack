package ru.livetyping.extensionpack.animations.transition

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.TransitionRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager

fun Context.transition(enterTransition: Int, exitTransition: Int) {
    (this as Activity).overridePendingTransition(enterTransition, exitTransition)
}

fun Activity.animate(intent: Intent, transitionImage: View, EXTRA_IMAGE: String) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionImage, EXTRA_IMAGE)
    ActivityCompat.startActivity(this, intent, options.toBundle())
}

inline fun Transition.addListener(
        crossinline onTransitionEnd: (Transition) -> Unit = {},
        crossinline onTransitionResume: (Transition) -> Unit = {},
        crossinline onTransitionPause: (Transition) -> Unit = {},
        crossinline onTransitionCancel: (Transition) -> Unit = {},
        crossinline onTransitionStart: (Transition) -> Unit = {}
) {
    addListener(object : Transition.TransitionListener {
        override fun onTransitionEnd(transition: Transition) {
            onTransitionEnd(transition)
        }

        override fun onTransitionResume(transition: Transition) {
            onTransitionResume(transition)
        }

        override fun onTransitionPause(transition: Transition) {
            onTransitionPause(transition)
        }

        override fun onTransitionCancel(transition: Transition) {
            onTransitionCancel(transition)
        }

        override fun onTransitionStart(transition: Transition) {
            onTransitionStart(transition)
        }

    })
}

inline fun Transition.onTransitionEnd(crossinline onTransitionEnd: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionEnd = onTransitionEnd)
}

inline fun Transition.onTransitionResume(crossinline onTransitionResume: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionResume = onTransitionResume)
}

inline fun Transition.onTransitionPause(crossinline onTransitionPause: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionPause = onTransitionPause)
}

inline fun Transition.onTransitionCancel(crossinline onTransitionCancel: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionCancel = onTransitionCancel)
}

inline fun Transition.onTransitionStart(crossinline onTransitionStart: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionStart = onTransitionStart)
}

class TransitionEndListener(val onEnd: (transition: Transition) -> Unit) : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition) = onEnd(transition)
    override fun onTransitionResume(transition: Transition) {}
    override fun onTransitionPause(transition: Transition) {}
    override fun onTransitionCancel(transition: Transition) {}
    override fun onTransitionStart(transition: Transition) {}
}

fun Transition.addEndListener(onEnd: (transition: Transition) -> Unit) {
    addListener(TransitionEndListener(onEnd))
}

inline fun ViewGroup.transitionAuto(builder: AutoTransition.() -> Unit = {}) {
    val transition = AutoTransition()
    transition.builder()
    TransitionManager.beginDelayedTransition(this, transition)
}

inline fun ViewGroup.transitionDelayed(@TransitionRes id: Int, builder: Transition.() -> Unit = {}) {
    val transition = TransitionInflater.from(context).inflateTransition(id)
    transition.builder()
    TransitionManager.beginDelayedTransition(this, transition)
}
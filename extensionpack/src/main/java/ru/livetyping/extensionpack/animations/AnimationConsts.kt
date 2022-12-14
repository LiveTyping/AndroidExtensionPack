package ru.livetyping.extensionpack.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.annotation.ColorRes
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

object AnimationConstants {
    const val MEDIUM = 400L
    val LINEAR_OUT_SLOW_IN by lazy { LinearOutSlowInInterpolator() }
}

fun View.rotate(
    initRotation: Int? = null, rotation: Int = 0,
    initAlpha: Float? = null, alpha: Float = 1f, startDelay: Long = 0,
    build: (ViewPropertyAnimator.() -> Unit)? = null
) {
    initRotation?.let { this.rotation = initRotation.toFloat() }
    initAlpha?.let { this.alpha = initAlpha }
    if (alpha == 1f) visibility = View.VISIBLE
    val anim = animate()
        .alpha(alpha)
        .rotation(rotation.toFloat())
        .setStartDelay(startDelay)
        .setDuration(AnimationConstants.MEDIUM)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (alpha == 0f) visibility = View.INVISIBLE
            }
        })
    build?.let { anim.build() }
    anim.start()
}

fun View.rotateIn(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) =
    rotate(initRotation = -180, initAlpha = 0f, startDelay = startDelay, build = build)

fun View.rotateOut(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) =
    rotate(rotation = 180, alpha = 0f, startDelay = startDelay, build = build)

fun colorFade(
    @ColorRes from: Int,
    @ColorRes to: Int,
    build: (ValueAnimator.() -> Unit)? = null,
    update: (Int) -> Unit
) {
    val anim = ValueAnimator.ofObject(ArgbEvaluator(), from, to)
    anim.duration = AnimationConstants.MEDIUM
    anim.interpolator = AnimationConstants.LINEAR_OUT_SLOW_IN
    anim.addUpdateListener { update(it.animatedValue as Int) }
    build?.let { anim.build() }
    anim.start()
}

fun View.colorFade(@ColorRes from: Int? = null, @ColorRes to: Int, build: (ValueAnimator.() -> Unit)? = null) {
    if (background !is ColorDrawable) {
        throw IllegalArgumentException("View needs to have a ColorDrawable background in order to animate the color")
    }
    val fromColor = from ?: (background as ColorDrawable).color
    colorFade(fromColor, to, build) { setBackgroundColor(it) }
}
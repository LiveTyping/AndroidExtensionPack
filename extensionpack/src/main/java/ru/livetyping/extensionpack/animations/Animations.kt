package ru.livetyping.extensionpack.animations

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import ru.livetyping.extensionpack.R
import ru.livetyping.extensionpack.view.invisible

fun View.slideDown(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
}

fun View.slideUp(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
}

fun View.animate(context: Context, animation: Int) {
    this.startAnimation(AnimationUtils.loadAnimation(context, animation))
}

fun View.leftToRight(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_right))
}

fun View.rightToLeft(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.right_to_left))
}

fun View.fadeOut(
    offset: Long = 0L,
    duration: Long = 200L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            invisible()
            onFinish?.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}
package ru.livetyping.extensionpack.animations.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.SidePropagation
import androidx.transition.TransitionManager
import androidx.transition.TransitionValues
import ru.livetyping.extensionpack.animations.transition.interpolators.LINEAR_OUT_SLOW_IN
import ru.livetyping.extensionpack.animations.transition.utils.LARGE_EXPAND_DURATION

class StaggerTransition : Fade(IN) {

    init {
        duration = LARGE_EXPAND_DURATION / 2
        interpolator = LINEAR_OUT_SLOW_IN
        propagation = SidePropagation().apply {
            setSide(Gravity.BOTTOM)
            setPropagationSpeed(1f)
        }
    }

    override fun createAnimator(
            sceneRoot: ViewGroup,
            startValues: TransitionValues?,
            endValues: TransitionValues?
    ): Animator? {
        val view = startValues?.view ?: endValues?.view ?: return null
        val fadeAnimator = super.createAnimator(sceneRoot, startValues, endValues) ?: return null
        return AnimatorSet().apply {
            playTogether(
                    fadeAnimator,
                    ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.height * 0.5f, 0f)
            )
        }
    }
}

inline fun RecyclerView.stagger(staggerTransition: StaggerTransition.() -> Unit = {}) {
    val stagger = StaggerTransition()
    stagger.staggerTransition()
    TransitionManager.beginDelayedTransition(this, stagger)
}

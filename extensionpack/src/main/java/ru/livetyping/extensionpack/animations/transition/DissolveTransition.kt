package ru.livetyping.extensionpack.animations.transition

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.view.drawToBitmap
import androidx.transition.Transition
import androidx.transition.TransitionValues

class DissolveTransition : Transition() {

    companion object {
        private const val PROPNAME_BITMAP = "com.example.android.motion.demo.dissolve:bitmap"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_BITMAP] = transitionValues.view.drawToBitmap()
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }
        val startBitmap = startValues.values[PROPNAME_BITMAP] as Bitmap
        val endBitmap = endValues.values[PROPNAME_BITMAP] as Bitmap

        if (startBitmap.sameAs(endBitmap)) {
            return null
        }

        val view = endValues.view
        val startDrawable = BitmapDrawable(view.resources, startBitmap).apply {
            setBounds(0, 0, startBitmap.width, startBitmap.height)
        }

        val overlay = view.overlay
        overlay.add(startDrawable)

        return ObjectAnimator
            .ofInt(startDrawable, "alpha", 255, 0).apply {
                doOnEnd {
                    overlay.remove(startDrawable)
                }
            }
    }
}
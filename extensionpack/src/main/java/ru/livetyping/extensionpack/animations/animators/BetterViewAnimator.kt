package ru.livetyping.extensionpack.animations.animators

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ViewAnimator
import ru.livetyping.extensionpack.R

open class BetterViewAnimator @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null
) : ViewAnimator(context, attrs) {

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        attrs.apply {
            val typedArray = context.obtainStyledAttributes(
                this,
                R.styleable.BetterViewAnimator,
                0,
                0
            )
            if (typedArray.hasValue(R.styleable.BetterViewAnimator_visible_child)) {
                visibleChildId = typedArray.getResourceId(
                    R.styleable.BetterViewAnimator_visible_child,
                    0
                )
            }
            typedArray.recycle()
            invalidate()
        }
    }
    var visibleChildId: Int
        get() = getChildAt(displayedChild).id
        set(id) {
            if (visibleChildId == id) return
            for (i in 0 until childCount) {
                if (getChildAt(i).id == id) {
                    displayedChild = i
                    return
                }
            }
            throw IllegalArgumentException("No view with ID $id")
        }
}
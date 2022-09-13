package ru.livetyping.extensionpack

import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.roundToInt

fun ViewPager2.listener(
    onPageScrollStateChanged: (state: Int) -> Unit = { _ -> },
    onPageSelected: (position: Int) -> Unit = { _ -> },
    onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> }
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}

fun ViewPager2.onPageScrollStateChanged(onPageScrollStateChanged: (state: Int) -> Unit = { _ -> }) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            onPageScrollStateChanged(state)
        }
    })
}

fun ViewPager2.onPageSelected(
    onPageSelected: (position: Int) -> Unit = { _ -> }
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}

fun ViewPager2.onPageScrolled(
    onPageSelected: (position: Int) -> Unit = { _ -> }
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}

fun ViewPager2.back(animate: Boolean = true) {
    setCurrentItem(currentItem - 1, animate)
}

fun ViewPager2.forward(animate: Boolean = true) {
    setCurrentItem(currentItem + 1, animate)
}

fun ViewPager2.isOnLastPage(): Boolean {
    return currentItem == (adapter?.itemCount ?: 0) - 1
}

fun ViewPager2.isOnFirstPage(): Boolean {
    return currentItem == 0
}

fun ViewPager2.canGoBack() = currentItem > 0

fun ViewPager2.canGoNext() = adapter != null && currentItem < adapter!!.itemCount - 1

fun ViewPager2.goPrevious() {
    if (canGoBack()) currentItem -= 1
}

fun ViewPager2.goNext() {
    if (canGoNext()) currentItem += 1
}

val ViewPager2.length: Int?
    get() = adapter?.itemCount

val ViewPager2.lastIndex: Int?
    get() = adapter?.itemCount?.minus(1)

val ViewPager2.isLastView: Boolean
    get() = currentItem == length?.minus(1)

fun ViewPager2.next() {
    if (!isLastView) {
        currentItem += 1
    }
}

fun ViewPager2.next(lastCallback: () -> Unit) {
    if (!isLastView) {
        currentItem += 1
    } else {
        lastCallback()
    }
}

val ViewPager2.recyclerView: RecyclerView
    get() = getChildAt(0) as RecyclerView

fun ViewPager2.nextCircular() {
    if (!isLastView) {
        currentItem += 1
    } else {
        currentItem = 0
    }
}

fun ViewPager2.setPageWidth(@Px size: Float) {
    post {
        val paddingSize = (width - (width * size)).roundToInt() / 2
        recyclerView.setPadding(paddingSize, 0, paddingSize, 0)
        recyclerView.clipToPadding = false
    }
}

fun ViewPager2.syncScrolling(other: ViewPager2, multiplier: Float = 1f) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager2.SCROLL_STATE_IDLE -> {
                    other.endFakeDrag()
                    other.currentItem = currentItem
                }

                ViewPager2.SCROLL_STATE_DRAGGING -> {
                    other.beginFakeDrag()
                }

                ViewPager2.SCROLL_STATE_SETTLING -> {

                }
            }
        }
    })

    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            other.fakeDragBy(-dx.toFloat() / multiplier)
        }
    })
}

fun ViewPager2.setFragmentAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle,
    fragments: List<Fragment>
) {
    adapter = object : FragmentStateAdapter(manager, lifecycle) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }
}

inline fun ViewPager2.doOnPageSelected(
    crossinline action: (position: Int) -> Unit
) = addOnPageChangeCallback(onPageSelected = action)

inline fun ViewPager2.addOnPageChangeCallback(
    crossinline onPageScrolled: (
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) -> Unit = { _, _, _ -> },
    crossinline onPageSelected: (Int) -> Unit = { _ -> },
    crossinline onPageScrollStateChanged: (Int) -> Unit = { _ -> }
): ViewPager2.OnPageChangeCallback {
    val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            onPageScrolled.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected.invoke(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            onPageScrollStateChanged.invoke(state)
        }
    }
    registerOnPageChangeCallback(listener)
    return listener
}

fun ViewPager2.setShowSideItems(pageMarginPx: Int, offsetPx: Int) {
    clipChildren = false
    clipToPadding = false
    offscreenPageLimit = 2
    setPadding(offsetPx, 0, offsetPx, 0)
    setPageTransformer(MarginPageTransformer(pageMarginPx))
}
package com.example.cpu02351_local.touchgesturedemo

import android.content.Context
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class CustomFrameLayout2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollingParent, FrameLayout(context, attrs, defStyleAttr) {

    private var mChildHelper : NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    init {
        isNestedScrollingEnabled = true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
       startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return false
    }

    override fun getNestedScrollAxes(): Int {
        return ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // Perhaps do something here
        if (dy < 0 &&
                !findViewById<RecyclerView>(R.id.scrollContainer).canScrollVertically(-1)) {
            translationY = -dy.toFloat()
            consumed[1] = dy
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (dyUnconsumed > 0) {
            translationY = dyConsumed.toFloat()
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onStopNestedScroll(target: View) {
        mChildHelper.stopNestedScroll()
    }

}
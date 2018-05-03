package com.example.cpu02351_local.touchgesturedemo

import android.app.Activity
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

class CustomNestedScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private lateinit var mChild: View
    private var mDownX = 0
    private var mDownY = 0
    private var mTouchSlop = 0
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mFlingMinVelocity = 0
    private var mFlingmaxVelocity = 0
    private var mShouldIntercept = false


    init {
        val viewConfig = ViewConfiguration.get(context)!!
        mTouchSlop = viewConfig.scaledTouchSlop
        mFlingMinVelocity = viewConfig.scaledMinimumFlingVelocity * 10
        mFlingMinVelocity = viewConfig.scaledMaximumFlingVelocity

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        mScreenWidth = displayMetrics.widthPixels
        mScreenHeight = displayMetrics.heightPixels

        isScrollContainer = true
        isNestedScrollingEnabled = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mChild = getChildAt(0)
        mChild.isNestedScrollingEnabled = true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if ((dy < 0 && isChildScrolledToTop())) {
            Log.d("SCROLL", "$dy")
            Log.d("SCROLL", "Translate")
            translationY = dy.toFloat()
            consumed[1] = dy
            return
        }
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    private fun isChildScrolledToTop(): Boolean {
        return !ViewCompat.canScrollVertically(mChild, -1)
    }
}
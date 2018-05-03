package com.example.cpu02351_local.touchgesturedemo

import android.app.Activity
import android.content.Context
import android.support.v4.view.NestedScrollingParent
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout

class PullToCloseLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollingParent, FrameLayout(context, attrs, defStyleAttr) {

    private var screenHeight = -1
    private lateinit var mChildView : View
    private var flingThreshold = -1
    private var flingThresholdIgnoreInner = -1

    init {
        val vc = ViewConfiguration.get(context)!!
        flingThreshold = (vc.scaledMinimumFlingVelocity + vc.scaledMaximumFlingVelocity) / 6
        flingThresholdIgnoreInner = flingThreshold * 2
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
    }


    private fun smoothResetChildPosition() {
        mChildView.animate()
                .translationY(0f)
                .start()
    }

    private fun smoothEndActivity() {
        mChildView.animate().translationY(screenHeight.toFloat())
                .withEndAction {
                    (context as Activity).finish()
                }
                .start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mChildView = getChildAt(0)
    }

    override fun onStopNestedScroll(target: View) {
        if (mChildView.translationY < screenHeight / 2f) {
            smoothResetChildPosition()
        } else {
            smoothEndActivity()
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
       return true
    }

    private var canChangeDir = false

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        val targetTranslation = mChildView.translationY - dyUnconsumed
        if (dyUnconsumed <= 0) {
            mChildView.translationY = targetTranslation
            canChangeDir = true
        }

        if (dyConsumed > 0 && canChangeDir && mChildView.translationY != 0f) {
            mChildView.translationY = mChildView.translationY - dyConsumed
            mChildView.scrollBy(0, -dyConsumed)
            if (mChildView.translationY < 0) {
                canChangeDir = false
                mChildView.translationY = 0f
            }
        }
    }
}
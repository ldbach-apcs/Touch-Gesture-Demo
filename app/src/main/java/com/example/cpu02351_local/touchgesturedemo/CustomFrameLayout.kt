package com.example.cpu02351_local.touchgesturedemo

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.FrameLayout

class CustomFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var vc = ViewConfiguration.get(context)!!
    private var mTouchSlop = 0
    var isScrollingY = false
    var isScrollingLeft = false
    var firstX = -1
    var firstY = -1
    var thresholdY = -1
    var thresholdX = -1
    var halfScreen = -1
    var mDis = 0
    private var mVelocityTracker = VelocityTracker.obtain()!!
    private var flingThreshold = -1


    init {
        this.mTouchSlop = vc.scaledTouchSlop
        flingThreshold = (vc.scaledMinimumFlingVelocity + vc.scaledMaximumFlingVelocity) / 10
        thresholdY = mTouchSlop
        thresholdX = (mTouchSlop * 1.2).toInt() // Prioritize scrolling the list
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        halfScreen = displayMetrics.widthPixels / 2

    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        val movement = event!!.action
        mVelocityTracker.addMovement(event)
        when (movement) {
            MotionEvent.ACTION_DOWN -> {
                resetFlags()
                firstX = event.x.toInt()
                firstY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> when {
                isScrollingVertical(event.y) -> {
                    isScrollingY = true
                }
                isMovingLeft(event.x) -> {
                    isScrollingLeft = true
                    firstX = event.x.toInt()
                    firstY = event.y.toInt()
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun isScrollingVertical(y: Float): Boolean =
            !isScrollingLeft && Math.abs(y - firstY).toInt() >= thresholdY

    private fun isMovingLeft(x: Float): Boolean = !isScrollingY && (x - firstX).toInt() >= thresholdX

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val movement = event!!.action
        mVelocityTracker.addMovement(event)

        if (movement == MotionEvent.ACTION_MOVE) {
            if (Math.abs(event.x - firstX).toInt() > thresholdX) {
                translateLeft(event)
                fadeBackground(event)
                firstX = event.x.toInt()
            }
            return true
        }
        else if (movement == MotionEvent.ACTION_UP) {
            if (needReturnToPrev()) {
                returnToPrev()
            } else {
                getChildAt(1).animate()
                        .translationX(0f)
                        .withEndAction {
                            getChildAt(0).alpha = 0f
                        }
                        .start()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun returnToPrev() {
        getChildAt(0).animate()
                .alpha(0f)
                .start()

        getChildAt(1).animate()
                .translationX((halfScreen * 2).toFloat())
                .withEndAction({
                    (context as Main2Activity).finishNoAnim()
                })
                .start()
    }

    private fun fadeBackground(event: MotionEvent) {
        //  interpolate transparency from 65% - 5%
        //  base - 20 * ratio
        val base = 255 * .75f
        val interpolateRange = 50
        val ratio = event.x / (halfScreen * 1.4f)
        val alpha = (base - interpolateRange * ratio) / 255f
        getChildAt(0).alpha = alpha
    }

    private fun translateLeft(event: MotionEvent) {
        mDis += (event.x - firstX).toInt()
        this.getChildAt(1).translationX = Math.max(0f, mDis.toFloat())
    }

    private fun needReturnToPrev(): Boolean = isFlingDetected() || isSlideAcrossMiddle()

    private fun isSlideAcrossMiddle(): Boolean = mDis >= halfScreen

    private fun isFlingDetected(): Boolean {
        mVelocityTracker.computeCurrentVelocity(1000)
        return mVelocityTracker.xVelocity >= flingThreshold
    }

    private fun resetFlags() {
        isScrollingY = false
        isScrollingLeft = false
        mVelocityTracker.clear()
    }
}
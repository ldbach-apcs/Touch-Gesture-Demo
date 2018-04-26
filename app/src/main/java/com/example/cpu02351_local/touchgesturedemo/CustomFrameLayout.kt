package com.example.cpu02351_local.touchgesturedemo

import android.app.Activity
import android.content.Context
import android.support.v4.math.MathUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout

class CustomFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val DIRECTION_UP = 0
        const val DIRECTION_DOWN = 1
        const val DIRECTION_LEFT = 2
        const val DIRECTION_RIGHT = 3
        const val DIRECTION_UNSPECIFIED = 4
    }

    private var vc = ViewConfiguration.get(context)!!
    private var mTouchSlop = 0
    private var downX = -1
    private var downY = -1
    private var thresholdY = -1
    private var thresholdX = -1
    private var halfScreenWidth = -1
    private var halfScreenHeight= -1
    private var mDisX = 0
    private var mDisY = 0
    private var mDirection: Int = DIRECTION_UNSPECIFIED
    private var mVelocityTracker = VelocityTracker.obtain()!!
    private var flingThreshold = -1
    private lateinit var bg: View
    private lateinit var mChildView: View
    private lateinit var mScrollContainer: View
    private var mMaxX = 0f
    private var mMinX = 0f
    private var mMaxY = 0f
    private var mMinY = 0f
    private val base = 255 * .75f
    private val interpolateRange = 50

    init {
        this.mTouchSlop = vc.scaledTouchSlop
        flingThreshold = (vc.scaledMinimumFlingVelocity + vc.scaledMaximumFlingVelocity) / 6
        thresholdY = mTouchSlop
        thresholdX = mTouchSlop
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        halfScreenWidth = displayMetrics.widthPixels / 2
        halfScreenHeight = displayMetrics.heightPixels / 2
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bg = getChildAt(0)
        mChildView = getChildAt(1)
        mScrollContainer = findViewById(R.id.scrollContainer)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        val movement = event!!.actionMasked
        mVelocityTracker.addMovement(event)
        when (movement) {
            MotionEvent.ACTION_DOWN -> {
                resetFlags()
                downX = event.rawX.toInt()
                downY = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                mDirection = getInitialScrollingDirection(event)
                var shouldIntercept = true
                when (mDirection) {
                    DIRECTION_RIGHT, DIRECTION_LEFT -> {
                        mMaxX = (halfScreenWidth * 2).toFloat()
                        mMinX = (-halfScreenWidth * 2).toFloat()
                        mMinY = 0f
                        mMaxY = 0f
                    }
                    DIRECTION_UP, DIRECTION_DOWN -> {
                        mMaxX = 0f
                        mMinX = 0f
                        mMinY = (-halfScreenHeight * 2).toFloat()
                        mMaxY = (halfScreenHeight * 2).toFloat()
                    }
                    DIRECTION_UNSPECIFIED -> shouldIntercept = false
                }

                if (shouldIntercept) {
                    downX = event.rawX.toInt()
                    downY = event.rawY.toInt()
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun getInitialScrollingDirection(event: MotionEvent): Int {
        val difX = event.rawX - downX
        val difY = event.rawY - downY
        return when {
            difX > thresholdX -> DIRECTION_RIGHT
            difX < -thresholdX -> DIRECTION_LEFT
            difY > thresholdY -> DIRECTION_DOWN
            difY < -thresholdY -> DIRECTION_UP
            else -> DIRECTION_UNSPECIFIED
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val movement = event!!.actionMasked
        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()
        mVelocityTracker.addMovement(event)
        if (movement == MotionEvent.ACTION_MOVE) {
            var shouldTranslate = true
            changeScrollingDirectionIfNeeded(rawX, rawY)
            when (mDirection) {
                DIRECTION_RIGHT -> {
                    if (mScrollContainer.canScrollHorizontally(1)) {
                        mScrollContainer.scrollBy (downX - rawX, 0)
                        shouldTranslate = false
                    }
                }
                DIRECTION_LEFT -> {
                    if (mScrollContainer.canScrollHorizontally(-1)) {
                        mScrollContainer.scrollBy (downX - rawX, 0)
                        shouldTranslate = false
                    }
                }
                DIRECTION_UP -> {
                    if (mScrollContainer.canScrollVertically(1)) {
                        mScrollContainer.scrollBy(0, downY - rawY)
                        shouldTranslate = false
                    }
                }
                DIRECTION_DOWN -> {
                    if (mScrollContainer.canScrollVertically(-1)) {
                        mScrollContainer.scrollBy (0,  downY - rawY)
                        shouldTranslate = false
                    }
                }
            }

            if (shouldTranslate) {
                fadeBackgroundView(event)
                translateChildView(event)
            } else {
                downX = rawX
                downY = rawY
            }
        }

        if (movement == MotionEvent.ACTION_UP) {
            if (needEndActivity()) {
                smoothEndActivity()
            } else {
                resetChildViewPosition()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun changeScrollingDirectionIfNeeded(newX: Int, newY: Int) {
        when (mDirection) {
            DIRECTION_UP -> {
                if (newY > downY) mDirection = DIRECTION_DOWN
            }
            DIRECTION_DOWN -> {
                if (newY < downY) mDirection = DIRECTION_UP
            }
            DIRECTION_LEFT -> {
                if (newX > downX) mDirection = DIRECTION_LEFT
            }
            DIRECTION_RIGHT -> {
                if (newX < downX) mDirection = DIRECTION_RIGHT
            }
        }
    }

    private fun resetChildViewPosition() {
        mChildView.animate()
                .translationX(0f)
                .translationY(0f)
                .withEndAction {
                    bg.alpha = 0f
                }
                .start()
    }

    private fun translateChildView(event: MotionEvent) {
        mDisX = (event.rawX - downX).toInt()
        mDisY = (event.rawY - downY).toInt()
        mChildView.apply {
            translationX = MathUtils.clamp(mDisX.toFloat(), mMinX, mMaxX)
            translationY = MathUtils.clamp(mDisY.toFloat(), mMinY, mMaxY)
        }
    }

    private fun smoothEndActivity() {
        bg.animate()
                .alpha(0f)
                .start()

        when (mDirection) {
            DIRECTION_LEFT -> mChildView.animate()
                    .translationX(mMinX)
                    .withEndAction {
                        (context as Activity).finish()
                    }
                    .start()

            DIRECTION_RIGHT -> mChildView.animate()
                    .translationX(mMaxX)
                    .withEndAction {
                        (context as Activity).finish()
                    }
                    .start()

            DIRECTION_DOWN -> mChildView.animate().
                    translationY(mMaxY)
                    .withEndAction {
                        (context as Activity).finish()
                    }
                    .start()

            DIRECTION_UP -> mChildView.animate()
                    .translationY(mMinY)
                    .withEndAction {
                        (context as Activity).finish()
                    }
                    .start()
        }
    }

    private fun fadeBackgroundView(event: MotionEvent) {
        //  interpolate transparency from 65% - 5%
        //  base - 20 * ratio
        val ratio = event.x / (halfScreenWidth * 1.4f)
        val alpha = (base - interpolateRange * ratio) / 255f
        bg.alpha = alpha
    }

    private fun needEndActivity(): Boolean = isFlingDetected() || isSlideAcrossMiddle()

    private fun isSlideAcrossMiddle(): Boolean = when (mDirection) {
        DIRECTION_LEFT -> mDisX <= -halfScreenWidth
        DIRECTION_RIGHT -> mDisX >= halfScreenWidth
        DIRECTION_DOWN -> mDisY >= halfScreenHeight
        DIRECTION_UP -> mDisY <= -halfScreenHeight
        else -> false
    }

    private fun isFlingDetected(): Boolean {
        mVelocityTracker.computeCurrentVelocity(1000)
        val velocX = mVelocityTracker.xVelocity
        val velocY = mVelocityTracker.yVelocity
        return when (mDirection) {
            DIRECTION_LEFT -> velocX <= -flingThreshold
            DIRECTION_RIGHT -> velocX >= +flingThreshold
            DIRECTION_DOWN -> velocY >= flingThreshold
            DIRECTION_UP -> velocY <= -flingThreshold
            else -> false
        }
    }

    private fun resetFlags() {
        mDirection = DIRECTION_UNSPECIFIED
        mVelocityTracker.clear()
    }
}
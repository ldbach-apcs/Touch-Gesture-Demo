package com.example.cpu02351_local.touchgesturedemo

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.view.View

class OuterNestedScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.d("OuterNestedScrollView", "onNestedPreScroll")
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        Log.d("OuterNestedScrollView", "onNestedScrollAccepted")
        super.onNestedScrollAccepted(child, target, nestedScrollAxes)
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        Log.d("OuterNestedScrollView", "onStartNestedScroll")
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.d("OuterNestedScrollView", "onNestedScroll")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun stopNestedScroll(type: Int) {
        Log.d("OuterNestedScrollView", "stopNestedScroll")
        super.stopNestedScroll(type)
    }

    override fun onStopNestedScroll(target: View) {
        Log.d("OuterNestedScrollView", "onStopNestedScroll")
        super.onStopNestedScroll(target)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean {
        Log.d("OuterNestedScrollView", "dispatchNestedScroll")
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        Log.d("OuterNestedScrollView", "startNestedScroll")
        return super.startNestedScroll(axes, type)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        Log.d("OuterZZZ", "PreFling")
        return super.onNestedPreFling(target, velocityX, velocityY)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        Log.d("OuterZZZ", "Fling")
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        Log.d("OuterZZZ", "dispatchPreFling")
        return super.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        Log.d("OuterZZZ", "dispatchFling")
        return super.dispatchNestedFling(velocityX, velocityY, consumed)
    }
}
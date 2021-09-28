package com.example.categorieslayoutiii

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import kotlin.math.max

class CategoriesLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout() called with: changed = $changed, l = $l, t = $t, r = $r, b = $b")

        var offset = paddingLeft
        val rightBound = r - l - paddingRight

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val lp = child.layoutParams as MyLayoutParams

            val childL = offset + lp.leftMargin
            val childT = paddingTop + lp.topMargin
            val childR = childL + child.measuredWidth
            val childB = childT + child.measuredHeight

            if (childR <= rightBound) {
                child.layout(childL, childT, childR, childB)
                offset = childR + lp.rightMargin
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var requestedWidth = 0
        var requestedHeight = 0
        var childMeasuredState = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val lp = child.layoutParams as MyLayoutParams

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

            requestedWidth += child.measuredWidth + lp.leftMargin + lp.rightMargin
            requestedHeight =
                max(requestedHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)

            childMeasuredState = combineMeasuredStates(childMeasuredState, child.measuredState)
        }

        requestedHeight += paddingTop + paddingBottom
        requestedWidth += paddingLeft + paddingRight

        requestedHeight = max(suggestedMinimumHeight, requestedHeight)
        requestedWidth = max(suggestedMinimumWidth, requestedWidth)

        setMeasuredDimension(
            resolveSizeAndState(requestedWidth, widthMeasureSpec, childMeasuredState),
            resolveSizeAndState(
                requestedHeight,
                heightMeasureSpec,
                childMeasuredState shl MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent() called with: event = $event")
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "onInterceptTouchEvent() called with: ev = $ev")
        return true
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MyLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        if (p is MarginLayoutParams) {
            return MyLayoutParams(p)
        }
        return MyLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MyLayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    override fun checkLayoutParams(p: LayoutParams?): Boolean = p is MyLayoutParams

    class MyLayoutParams : MarginLayoutParams {
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(params: MarginLayoutParams) : super(params)
        constructor(params: LayoutParams?) : super(params)
    }

    companion object {
        private const val TAG = "CategoriesLayout"
    }
}
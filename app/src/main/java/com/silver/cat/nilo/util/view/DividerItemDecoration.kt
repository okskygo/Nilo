package com.silver.cat.nilo.util.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View

import com.silver.cat.nilo.R

class DividerItemDecoration : RecyclerView.ItemDecoration {

    enum class Type {
        TOP, BOTTOM, BOTH
    }

    private var mDivider: Drawable?
    private var type: Type
    private var needlessDividers = 1

    constructor(context: Context?, type: Type) {
        mDivider = context?.let { ContextCompat.getDrawable(it, R.drawable.shape_recycler_divider) }
        this.type = type
    }

    constructor(context: Context?, type: Type, needlessDividers: Int) {
        mDivider = context?.let { ContextCompat.getDrawable(it, R.drawable.shape_recycler_divider) }
        this.type = type
        this.needlessDividers = needlessDividers
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.adapter.itemCount
        if (childCount == 0) {
            return
        }
        for (i in 0 until childCount - needlessDividers) {
            val child = parent.getChildAt(i) ?: return
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (type == Type.TOP || type == Type.BOTH) {
                drawTopLine(c, left, right, child, params)
            }
            if (type == Type.BOTTOM || type == Type.BOTH) {
                drawBottomLine(c, left, right, child, params)
            }
        }
    }

    private fun drawBottomLine(c: Canvas,
                               paddingLeft: Int,
                               paddingRight: Int,
                               child: View,
                               params: RecyclerView.LayoutParams) {
        val mDivider = mDivider ?: return
        val top = child.bottom
        val bottom = top + mDivider.intrinsicHeight
        val left = paddingLeft + params.leftMargin
        val right = paddingRight - params.rightMargin

        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(c)
    }

    private fun drawTopLine(c: Canvas,
                            paddingLeft: Int,
                            paddingRight: Int,
                            child: View,
                            params: RecyclerView.LayoutParams) {
        val mDivider = mDivider ?: return
        val bottom = child.top
        val top = bottom - mDivider.intrinsicHeight
        val left = paddingLeft + params.leftMargin
        val right = paddingRight - params.rightMargin
        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(c)
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State?) {
        if (parent.getChildAdapterPosition(view) != parent.adapter.itemCount - needlessDividers) {
            if (type == Type.TOP || type == Type.BOTH) {
                rectAppendDividerTopHeight(outRect)
            }
            if (type == Type.BOTTOM || type == Type.BOTH) {
                rectAppendDividerBottomHeight(outRect)
            }
        }
    }

    private fun rectAppendDividerBottomHeight(outRect: Rect) {
        outRect.bottom = mDivider?.intrinsicHeight ?: 0
    }

    private fun rectAppendDividerTopHeight(outRect: Rect) {
        outRect.top = mDivider?.intrinsicHeight ?: 0
    }

}

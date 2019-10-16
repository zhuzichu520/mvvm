package com.zhuzichu.android.mvvm.binding.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.zhuzichu.android.libs.tool.dp2px

class DividerLine(mContext: Context) : RecyclerView.ItemDecoration() {

    companion object {
        private const val DEFAULT_DIVIDER_SIZE = 0.5
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    private val dividerDrawable: Drawable?
    private var dividerSize: Int = 0
    private var mode: LineDrawMode? = null

    enum class LineDrawMode {
        HORIZONTAL, VERTICAL, BOTH
    }

    init {
        val attrArray = mContext.obtainStyledAttributes(ATTRS)
        dividerDrawable = attrArray.getDrawable(0)
        attrArray.recycle()
    }

    constructor(context: Context, mode: LineDrawMode) : this(context) {
        this.mode = mode
    }

    constructor(context: Context, dividerSize: Int, mode: LineDrawMode) : this(context, mode) {
        this.dividerSize = dividerSize
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        checkNotNull(mode) { "assign LineDrawMode,please!" }
        when (mode) {
            LineDrawMode.VERTICAL -> drawVertical(c, parent)
            LineDrawMode.HORIZONTAL -> drawHorizontal(c, parent)
            LineDrawMode.BOTH -> {
                drawHorizontal(c, parent)
                drawVertical(c, parent)
            }
        }
    }

    private fun drawVertical(
        c: Canvas,
        parent: RecyclerView
    ) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val left = child.right + params.rightMargin
            val right =
                if (dividerSize == 0) left + dp2px(
                    parent.context,
                    DEFAULT_DIVIDER_SIZE.toFloat()
                ) else left + dividerSize
            dividerDrawable!!.setBounds(left, top, right, bottom)
            dividerDrawable.draw(c)
        }
    }


    private fun drawHorizontal(
        c: Canvas,
        parent: RecyclerView
    ) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val top = child.bottom + params.topMargin
            val right = child.right - params.rightMargin
            val bottom =
                if (dividerSize == 0) top + dp2px(
                    parent.context,
                    DEFAULT_DIVIDER_SIZE.toFloat()
                ) else top + dividerSize
            dividerDrawable!!.setBounds(left, top, right, bottom)
            dividerDrawable.draw(c)
        }
    }

}
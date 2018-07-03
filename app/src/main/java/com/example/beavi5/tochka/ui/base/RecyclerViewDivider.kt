package com.example.beavi5.tochka.ui.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView

class RecyclerViewDivider(context: Context) : RecyclerView.ItemDecoration() {
    private val divider: Drawable?

    init {
        val attrs = intArrayOf(android.R.attr.listDivider)
        divider = context.obtainStyledAttributes(attrs).getDrawable(0)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount - 1) {
            val item = parent.getChildAt(i)

            val top = item.bottom + (item.layoutParams as RecyclerView.LayoutParams).bottomMargin
            val bottom = top + divider!!.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}
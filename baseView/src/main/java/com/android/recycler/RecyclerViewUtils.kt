package com.android.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Author: han.chen
 * Time: 2022/4/7 11:21
 */
object RecyclerViewUtils {

    @JvmStatic
    fun findFirstVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is LinearLayoutManager) {
            layoutManager.findFirstVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val info = IntArray(layoutManager.spanCount)
            layoutManager.findLastVisibleItemPositions(info)
            return findMin(info)
        }
        return 0
    }

    @JvmStatic
    fun findLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is LinearLayoutManager) {
            return layoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val info = IntArray(layoutManager.spanCount)
            layoutManager.findLastVisibleItemPositions(info)
            return findMax(info)
        }
        return 0
    }

    private fun findMin(info: IntArray): Int {
        var min = info[0]
        for (value in info) {
            if (value < min) {
                min = value
            }
        }
        return min
    }

    private fun findMax(info: IntArray): Int {
        var max = info[0]
        for (value in info) {
            if (value > max) {
                max = value
            }
        }
        return max
    }
}
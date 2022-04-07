package com.android.recycler

/**
 * Author: han.chen
 * Time: 2022/4/7 11:09
 */
interface OnLoadMoreHelper {

    fun canLoadMore(): Boolean

    fun onLoadMore()
}
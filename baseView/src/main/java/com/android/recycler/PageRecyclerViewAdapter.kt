package com.android.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.widget.R


/**
 * Author: han.chen
 * Time: 2022/4/7 10:54
 */
abstract class PageRecyclerViewAdapter<T>(context: Context) : BaseRecyclerAdapter<T>(context), OnLoadMoreListener {
    var preLoadNum = 1
    private var isLoading: Boolean = false
    private var isError: Boolean = false
    private var moreFooterView: View? = null
    private var errorFooterView: View? = null
    private var completeFooterView: View? = null
    var onLoadMoreHelper: OnLoadMoreHelper? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        if (onLoadMoreHelper == null || recyclerView.layoutManager == null) {
            return
        }
        enableLoadMore(recyclerView)
    }

    private fun enableLoadMore(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx == 0 && dy == 0) {
                    return
                }
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager != null) {
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem =
                        RecyclerViewUtils.findLastVisibleItemPosition(layoutManager)
                    if (!isLoading && !isError && totalItemCount <= lastVisibleItem + preLoadNum) {
                        onLoadMoreHelper?.run {
                            if (canLoadMore()) {
                                if (!hasFooterView()) {
                                    addFooterView(false)
                                }
                            }
                            isLoading = true
                            onLoadMore()
                        }
                    }
                }

            }
        })
    }

    private fun addFooterView(error: Boolean) {
        footerView = if (error) {
            generateErrorFooterView()
        } else {
            generateMoreFooterView()
        }
        if (footerView == null) {
            notifyItemInserted(itemCount)
        } else {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun generateMoreFooterView(): View? {
        if (moreFooterView == null) {
            moreFooterView = LayoutInflater.from(context)
                .inflate(R.layout.base_loading_view, recyclerView, false)
        }
        return moreFooterView
    }

    private fun generateErrorFooterView(): View? {
        if (errorFooterView == null) {
            errorFooterView = LayoutInflater.from(context)
                .inflate(R.layout.base_error_view, recyclerView, false)
            errorFooterView?.setOnClickListener {
                footerView = generateMoreFooterView()
                notifyItemChanged(itemCount - 1)
                onLoadMoreHelper?.onLoadMore()
            }
        }
        return errorFooterView
    }

    private fun generateCompletedFooterView():View? {
        if (completeFooterView == null) {
            completeFooterView = LayoutInflater.from(context)
                .inflate(R.layout.base_complete_view, recyclerView, false)
        }
        return completeFooterView
    }

    private fun removeFooterView() {
        if (hasFooterView()) {
            notifyItemRemoved(itemCount - 1)
            footerView = null
        }
    }

    private fun addCompletedFooterView() {
        footerView = generateCompletedFooterView()
        if (hasFooterView()) {
            notifyItemInserted(itemCount)
        } else {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onLoadMoreCompleted() {
        isLoading = false
        isError = false
        onLoadMoreHelper?.run {
            if (!canLoadMore()) {
                removeFooterView()
                addCompletedFooterView()
            }
        }
    }

    override fun onLoadMoreFailed() {
        isLoading = false
        isError = true
        if (onLoadMoreHelper != null && onLoadMoreHelper?.canLoadMore() == true) {
            addFooterView(true)
        } else {
            removeFooterView()
        }
    }
}
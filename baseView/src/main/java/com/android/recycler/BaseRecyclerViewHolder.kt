package com.android.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: han.chen
 * Time: 2022/4/1 17:19
 */
abstract class BaseRecyclerViewHolder<T>(
    var context: Context?,
    parent: ViewGroup,
    @LayoutRes var layoutId: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(context).inflate(layoutId, parent, false)
) {

    abstract fun bindData(item: T?)
}
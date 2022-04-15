package com.bereality.baseview

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.android.recycler.BaseRecyclerViewHolder

/**
 * Author: han.chen
 * Time: 2022/4/6 11:24
 */
class MainViewHolder(context: Context, parent: ViewGroup) :
    BaseRecyclerViewHolder<String>(context, parent, R.layout.layout_item) {
    private val text = itemView.findViewById<TextView>(R.id.item_view)
    override fun bindData(item: String?) {
        text.text = item
    }
}
package com.android.recycler

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

/**
 * Author: han.chen
 * Time: 2022/4/1 17:24
 */
abstract class BaseRecyclerAdapter<T>(var context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val itemList: ArrayList<T> = ArrayList()

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getItem(position: Int): T? {
        return itemList[position]
    }

    fun isEmpty(): Boolean {
        return itemList.isEmpty()
    }

    @Synchronized
    open fun add(index: Int, item: T?) {
        item?.let {
            itemList.add(index, it)
            notifyItemInserted(index)
        }
    }

    @Synchronized
    open fun add(item: T?): Boolean {
        item?.let {
            val lastIndex = itemList.size
            return if (itemList.add(it)) {
                notifyItemInserted(lastIndex)
                true
            } else {
                false
            }
        }
        return false
    }

    @Synchronized
    open fun addAll(index: Int, collection: Collection<T>): Boolean {
        return if (itemList.addAll(index, collection)) {
            notifyItemRangeInserted(index, collection.size)
            true
        } else {
            false
        }
    }

    @Synchronized
    open fun addAll(collection: Collection<T>?): Boolean {
        collection?.let {
            val lastIndex = itemList.size
            return if (itemList.addAll(collection)) {
                notifyItemRangeChanged(lastIndex, it.size)
                true
            } else {
                false
            }
        }
        return false
    }

    @Synchronized
    open fun move(from: Int, to: Int) {
        Collections.swap(itemList, from, to)
        notifyItemMoved(from, to)
    }

    @Synchronized
    open fun remove(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    @Synchronized
    open fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}
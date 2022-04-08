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
abstract class BaseRecyclerAdapter<T>(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = Int.MIN_VALUE
    private val VIEW_TYPE_FOOTER = Int.MIN_VALUE + 1
    var headerView: View? = null
    var footerView: View? = null
    var recyclerView: RecyclerView? = null

    fun hasHeaderView(): Boolean {
        return headerView != null
    }

    fun hasFooterView(): Boolean {
        return footerView != null
    }

    fun isHeaderView(position: Int): Boolean {
        return headerView != null && position == 0
    }

    fun isFooterView(position: Int): Boolean {
        return footerView != null && position == itemCount - 1
    }

    protected val itemList: ArrayList<T> = ArrayList()

    private fun getFootViewGroup(): ViewGroup {
        val footViewGroup: ViewGroup = LinearLayout(context)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        footViewGroup.layoutParams = params
        return footViewGroup
    }

    abstract fun onCreateBasicItemViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract fun onBindBasicItemView(viewHolder: RecyclerView.ViewHolder, position: Int)

    abstract fun getBasicItemType(position: Int): Int

    abstract fun getBasicItemCount(): Int

    fun getItem(position: Int): T? {
        return itemList[position]
    }

    fun isEmpty(): Boolean {
        return itemList.isEmpty()
    }

    override fun getItemCount(): Int {
        var itemCount = getBasicItemCount()
        if (headerView != null) {
            itemCount += 1
        }
        if (footerView != null) {
            itemCount += 1
        }
        return itemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_HEADER) {
            headerView?.let {
                return HeaderViewHolder(it)
            }
        } else if (viewType == VIEW_TYPE_FOOTER) {
            return FooterViewHolder(getFootViewGroup())
        }
        return onCreateBasicItemViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && holder.itemViewType == VIEW_TYPE_HEADER) {
            setFullSpanIfNeed(holder)
        } else if (position == itemCount - 1 && holder.itemViewType == VIEW_TYPE_FOOTER) {
            setFullSpanIfNeed(holder)
            (holder.itemView as? ViewGroup)?.removeAllViews()
            (footerView?.parent as? ViewGroup)?.removeAllViews()
            footerView?.let {
                (holder.itemView as? ViewGroup)?.addView(it)
            }
        } else {
            val index = position - if (hasHeaderView()) 1 else 0
            onBindBasicItemView(holder, index)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && headerView != null) {
            return VIEW_TYPE_HEADER
        }
        if (position == itemCount - 1 && footerView != null) {
            return VIEW_TYPE_FOOTER
        }
        return getBasicItemType(position - if (hasHeaderView()) 1 else 0)
    }

    @Synchronized
    fun add(index: Int, item: T?) {
        item?.let {
            itemList.add(index, it)
            notifyItemInserted(index)
        }
    }

    @Synchronized
    fun add(item: T?): Boolean {
        item?.let {
            val lastIndex = itemList.size
            return if (itemList.add(it)) {
                notifyItemInserted(lastIndex + if (hasHeaderView()) 1 else 0)
                true
            } else {
                false
            }
        }
        return false
    }

    @Synchronized
    fun addAll(collection: Collection<T>?): Boolean {
        collection?.let {
            val lastIndex = itemList.size
            return if (itemList.addAll(collection)) {
                notifyItemRangeChanged(lastIndex + if (hasHeaderView()) 1 else 0, it.size)
                true
            } else {
                false
            }
        }
        return false
    }

    @Synchronized
    fun move(from: Int, to: Int) {
        Collections.swap(itemList, from, to)
        notifyItemMoved(
            from + if (hasHeaderView()) 1 else 0,
            to + if (hasHeaderView()) 1 else 0
        )
    }

    @Synchronized
    fun remove(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position + if (hasHeaderView()) 1 else 0)
    }

    @Synchronized
    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    private fun setFullSpanIfNeed(holder: RecyclerView.ViewHolder) {
        recyclerView?.apply {
            (layoutManager as? StaggeredGridLayoutManager)?.let {
                val params = StaggeredGridLayoutManager.LayoutParams(holder.itemView.layoutParams)
                params.isFullSpan = true
                holder.itemView.layoutParams = params
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

}
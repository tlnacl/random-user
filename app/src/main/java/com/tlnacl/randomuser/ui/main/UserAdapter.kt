package com.tlnacl.randomuser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tlnacl.randomuser.R
import com.tlnacl.randomuser.data.User

class UserAdapter(private val userClickCallback: UserViewHolder.UserClickCallback) :
    RecyclerView.Adapter<UserViewHolder>() {
    private var items: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, userClickCallback)
    }

    fun getItems() = items

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private var isLoadingNextPage: Boolean = false

    /**
     * @return true if value has changed since last invocation
     */
    fun setLoadingNextPage(loadingNextPage: Boolean): Boolean {
        val hasLoadingMoreChanged = loadingNextPage != isLoadingNextPage

        val notifyInserted = loadingNextPage && hasLoadingMoreChanged
        val notifyRemoved = !loadingNextPage && hasLoadingMoreChanged
        isLoadingNextPage = loadingNextPage

        if (notifyInserted) {
            notifyItemInserted(items.size)
        } else if (notifyRemoved) {
            notifyItemRemoved(items.size)
        }

        return hasLoadingMoreChanged
    }

    fun isLoadingNextPage(): Boolean {
        return isLoadingNextPage
    }

    fun setItems(newItems: List<User>) {
        val oldItems = this.items
        this.items = newItems

        if (oldItems.isEmpty()) {
            notifyDataSetChanged()
        } else {
            // Use Diff utils
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldItems.size
                }

                override fun getNewListSize(): Int {
                    return newItems.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldItems[oldItemPosition]
                    val newItem = newItems[newItemPosition]

                    if (oldItem is User
                        && newItem is User
                        && oldItem.id == newItem.id
                    ) {
                        return true
                    }

                    return false
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldItems[oldItemPosition]
                    val newItem = newItems[newItemPosition]

                    return oldItem == newItem
                }
            }, true).dispatchUpdatesTo(this)
        }
    }
}
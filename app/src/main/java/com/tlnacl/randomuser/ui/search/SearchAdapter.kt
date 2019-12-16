package com.tlnacl.randomuser.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlnacl.randomuser.R
import com.tlnacl.randomuser.data.User
import com.tlnacl.randomuser.ui.main.UserViewHolder

/**
 * Created by tomt on 21/06/17.
 */
class SearchAdapter(private val callback: UserViewHolder.UserClickCallback): RecyclerView.Adapter<UserViewHolder>(){
    private var users: List<User> = emptyList()

    fun setUsers(users:List<User>){
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItemCount()= users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(users[position])

}


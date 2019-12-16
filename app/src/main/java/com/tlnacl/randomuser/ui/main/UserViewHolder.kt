package com.tlnacl.randomuser.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.tlnacl.randomuser.R
import com.tlnacl.randomuser.data.User
import kotlinx.android.extensions.LayoutContainer

class UserViewHolder(itemView: View, private val callback: UserClickCallback) : RecyclerView.ViewHolder(itemView), LayoutContainer {
    fun bind(user: User){
        // android extentions Does not work for search view maybe because of two hold in same screen
        val name = itemView.findViewById<TextView>(R.id.text_name)
        val gender = itemView.findViewById<TextView>(R.id.text_gender)
        val dob = itemView.findViewById<TextView>(R.id.text_dob)
        val imageIcon = itemView.findViewById<ImageView>(R.id.image_icon)
        name.text = user.name
        gender.text = user.gender
        dob.text = user.dob
        imageIcon.load(user.thumbnailUrl)
        itemView.setOnClickListener { callback.onUserClicked(user) }
    }

    override val containerView: View?
        get() = itemView

    interface UserClickCallback {
        fun onUserClicked(user: User)
    }
}
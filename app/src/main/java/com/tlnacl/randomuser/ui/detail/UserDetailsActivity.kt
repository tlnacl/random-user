package com.tlnacl.randomuser.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.tlnacl.randomuser.R
import com.tlnacl.randomuser.data.User
import kotlinx.android.synthetic.main.item_user.*

class UserDetailsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        intent.getParcelableExtra<User>(EXTRA_USER).let { user ->
            text_name.text = user.name
            text_gender.text = user.gender
            text_dob.text = user.dob
            image_icon.load(user.imageUrl)
        }

    }

    companion object {
        private const val EXTRA_USER = "extra_user"

        fun starterIntent(context: Context, user: User): Intent {
            return Intent(context, UserDetailsActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
        }
    }
}
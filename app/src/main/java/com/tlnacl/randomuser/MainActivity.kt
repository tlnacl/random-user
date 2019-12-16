package com.tlnacl.randomuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tlnacl.randomuser.ui.main.MainFragment
import com.tlnacl.randomuser.ui.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Random User"
        toolbar.inflateMenu(R.menu.activity_main_toolbar)

        toolbar.setOnMenuItemClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.container, SearchFragment())
                .addToBackStack("Search")
                .commit()
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance())
                .commitNow()
        }
    }

}

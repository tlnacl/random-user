package com.tlnacl.randomuser.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tlnacl.randomuser.MainApplication
import com.tlnacl.randomuser.R
import com.tlnacl.randomuser.data.User
import com.tlnacl.randomuser.ui.detail.UserDetailsActivity
import com.tlnacl.randomuser.viewModelProvider
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.view_error.*
import timber.log.Timber
import javax.inject.Inject

class MainFragment : Fragment(), UserViewHolder.UserClickCallback {
    private lateinit var adapter: UserAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MainApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getHomeLiveData().observe(this, Observer { render(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        view_user_list.layoutManager = layoutManager
        adapter = UserAdapter(this)
        view_user_list.adapter = adapter

        view_user_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!adapter.isLoadingNextPage() && newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItems().size - 1)
                    viewModel.onUiEvent(MainUiEvent.LoadNextPage)
            }
        })
    }

    private fun render(viewState: MainViewState) {
        Timber.i("render %s", viewState)
        if (!viewState.loadingFirstPage && viewState.firstPageError == null) {
            renderShowData(viewState)
        } else if (viewState.loadingFirstPage) {
            renderFirstPageLoading()
        } else if (viewState.firstPageError != null) {
            renderFirstPageError()
        } else {
            throw IllegalStateException("Unknown view state " + viewState)
        }
    }

    private fun renderShowData(viewState: MainViewState) {
        view_loading.visibility = View.GONE
        view_error.visibility = View.GONE

        adapter.setLoadingNextPage(viewState.loadingNextPage)

        adapter.setItems(viewState.data)

        if (viewState.nextPageError != null) {
            Toast.makeText(activity, R.string.error_unknown, Toast.LENGTH_LONG).show()
        }
    }

    private fun renderFirstPageLoading() {
        view_loading.visibility = View.VISIBLE
        view_error.visibility = View.GONE
    }

    private fun renderFirstPageError() {
        view_loading.visibility = View.GONE
        view_error.visibility = View.VISIBLE
    }

    override fun onUserClicked(user: User) {
        val i = UserDetailsActivity.starterIntent(activity!!, user)
        activity!!.startActivity(i)
    }

}

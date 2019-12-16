package com.tlnacl.randomuser.ui.search

import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlnacl.randomuser.MainApplication
import com.tlnacl.randomuser.R
import com.tlnacl.randomuser.data.User
import com.tlnacl.randomuser.ui.detail.UserDetailsActivity
import com.tlnacl.randomuser.ui.main.UserViewHolder
import com.tlnacl.randomuser.viewModelProvider
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.view_error.*
import timber.log.Timber
import javax.inject.Inject

class SearchFragment : Fragment(), UserViewHolder.UserClickCallback {
    override fun onUserClicked(user: User) {
        val i = UserDetailsActivity.starterIntent(activity!!, user)
        activity!!.startActivity(i)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SearchViewModel


    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MainApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getSearchLiveData().observe(this, Observer { render(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                dismissKeyboard(searchView)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQuery(newText)
                return true
            }
        })
    }

    fun render(searchViewState: SearchViewState) {
        Timber.d("render:%s", searchViewState)
        when (searchViewState) {
            is SearchViewState.SearchNotStartedYet -> renderSearchNotStarted()
            is SearchViewState.EmptyResult -> renderEmptyResult()
            is SearchViewState.Loading -> renderLoading()
            is SearchViewState.SearchResult -> renderResult(searchViewState.result)
            is SearchViewState.Error -> renderError()
        }
    }

    private fun renderResult(result: List<User>) {
        TransitionManager.beginDelayedTransition(container)
        recyclerView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        emptyView.visibility = View.GONE
        view_error.visibility = View.GONE
        adapter.setUsers(result)
        adapter.notifyDataSetChanged()
    }

    private fun renderSearchNotStarted() {
        TransitionManager.beginDelayedTransition(container)
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.GONE
        view_error.visibility = View.GONE
        emptyView.visibility = View.GONE
    }

    private fun renderLoading() {
        TransitionManager.beginDelayedTransition(container)
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        view_error.visibility = View.GONE
        emptyView.visibility = View.GONE
    }

    private fun renderError() {
        TransitionManager.beginDelayedTransition(container)
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.GONE
        view_error.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
    }

    private fun renderEmptyResult() {
        TransitionManager.beginDelayedTransition(container)
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.GONE
        view_error.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
    }

    override fun onPause() {
        dismissKeyboard(searchView)
        super.onPause()
    }

    private fun dismissKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
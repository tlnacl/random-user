package com.tlnacl.randomuser.ui.search

import com.tlnacl.randomuser.data.User

sealed class SearchViewState {
    object SearchNotStartedYet : SearchViewState()
    object Loading : SearchViewState()
    object EmptyResult : SearchViewState()
    data class SearchResult(val result: List<User>) : SearchViewState()
    data class Error(val error: Throwable) : SearchViewState()
}
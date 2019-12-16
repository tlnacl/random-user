package com.tlnacl.randomuser.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tlnacl.randomuser.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val searchLiveData = MutableLiveData<SearchViewState>()

    init {
        searchLiveData.value = SearchViewState.SearchNotStartedYet
    }

    fun getSearchLiveData(): LiveData<SearchViewState> {
        return searchLiveData
    }

    fun onSearchQuery(query: String?) {
        if (query.isNullOrEmpty() || query.length < 2) {
            searchLiveData.value = SearchViewState.SearchNotStartedYet
        } else {
            viewModelScope.launch(Dispatchers.Default) {
                try {
                    searchLiveData.postValue(SearchViewState.Loading)

                    val users = userRepository.searchUsers(String.format("*%s*", query))
                    if (users.isEmpty()) searchLiveData.postValue(SearchViewState.EmptyResult)
                    else searchLiveData.postValue(SearchViewState.SearchResult(users))
                } catch (e: Exception) {
                    searchLiveData.postValue(SearchViewState.Error(e))
                }
            }
        }
    }

}
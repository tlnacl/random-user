package com.tlnacl.randomuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tlnacl.randomuser.data.UserRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val mainLiveData = MutableLiveData<MainViewState>()

    private var currentViewState = MainViewState(loadingFirstPage = true)
        set(value) {
            field = value
            mainLiveData.value = value
        }

    init {
        viewModelScope.launch { loadFirstPage() }
    }

    fun getHomeLiveData(): LiveData<MainViewState> {
        return mainLiveData
    }

    fun onUiEvent(uiEvent: MainUiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is MainUiEvent.LoadNextPage -> loadNextPage()
            }
        }
    }

    private suspend fun loadFirstPage() {
        Timber.d("loadFirstPage")
        currentViewState = MainViewState(loadingFirstPage = true)
        try {
            currentViewState = MainViewState(data = userRepository.firstLoad())
        } catch (e: Exception) {
            currentViewState = MainViewState(firstPageError = e)
        }
    }

    private suspend fun loadNextPage() {
        currentViewState = currentViewState.copy(loadingNextPage = true, nextPageError = null)
        try {
            currentViewState = currentViewState.copy(data = userRepository.getUsers(), loadingNextPage = false, nextPageError = null)
        } catch (e: Exception) {
            currentViewState = currentViewState.copy(loadingNextPage = false, nextPageError = e)
        }
    }
}

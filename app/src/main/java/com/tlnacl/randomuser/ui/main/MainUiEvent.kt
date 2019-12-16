package com.tlnacl.randomuser.ui.main

sealed class MainUiEvent {
    object LoadFirstPage : MainUiEvent()
    object LoadNextPage : MainUiEvent()
}
package com.tlnacl.randomuser.ui.main

import com.tlnacl.randomuser.data.User

data class MainViewState(val loadingFirstPage: Boolean = false,
                         val firstPageError: Throwable? = null,
                         val loadingNextPage: Boolean = false,
                         val nextPageError: Throwable? = null,
                         val data: List<User> = emptyList())
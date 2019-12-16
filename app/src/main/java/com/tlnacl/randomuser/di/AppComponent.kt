package com.tlnacl.randomuser.di

import com.tlnacl.randomuser.di.viewmodel.ViewModelModule
import com.tlnacl.randomuser.ui.main.MainFragment
import com.tlnacl.randomuser.ui.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [AppModule::class, ViewModelModule::class]
)
@Singleton
interface AppComponent {
    fun inject(into: MainFragment)
    fun inject(into: SearchFragment)
}
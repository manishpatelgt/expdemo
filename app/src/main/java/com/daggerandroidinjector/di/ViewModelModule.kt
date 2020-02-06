package com.daggerandroidinjector.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daggerandroidinjector.ui.main.MainActivityViewModel
import com.daggerandroidinjector.ui.mainfragments.MainFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    internal abstract fun bindMainFragmentViewModel(mainFragmentViewModel: MainFragmentViewModel): ViewModel

}
package com.daggerandroidinjector.di

import com.daggerandroidinjector.ui.mainfragments.MainFragment
import com.daggerandroidinjector.ui.mainfragments.MainFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun contributeMainFragment(): MainFragment
}
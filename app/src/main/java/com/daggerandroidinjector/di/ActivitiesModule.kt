package com.daggerandroidinjector.di

import com.daggerandroidinjector.ui.main.MainActivity
import com.daggerandroidinjector.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity
}
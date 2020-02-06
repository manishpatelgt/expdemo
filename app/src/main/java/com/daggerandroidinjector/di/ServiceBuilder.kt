package com.daggerandroidinjector.di

import com.daggerandroidinjector.services.MyService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector()
    abstract fun contributeMyServiceInjector(): MyService
}
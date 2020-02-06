package com.daggerandroidinjector.di

import android.content.Context
import com.daggerandroidinjector.application.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Manish Patel on 2/6/2020.
 */

@Singleton
@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        AndroidInjectionModule::class,
        ActivityBuilder::class,
        FragmentBuilder::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
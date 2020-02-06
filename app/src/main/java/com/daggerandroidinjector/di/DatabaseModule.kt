package com.daggerandroidinjector.di

import android.content.Context
import androidx.room.Room
import com.daggerandroidinjector.db.PostDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            PostDatabase::class.java,
            "post-database"
        ).build()
        /*Room.databaseBuilder(context, PostDatabase::class.java, "postDatabase").apply {
            addMigrations(PostDatabase.MIGRATION_1_2)
        }.build()*/

}
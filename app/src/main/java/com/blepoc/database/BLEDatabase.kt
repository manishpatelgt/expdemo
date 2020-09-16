package com.blepoc.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blepoc.utility.SingletonHolder
import com.blepoc.utility.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Manish Patel on 8/6/2020.
 */
private val ioScope = CoroutineScope(Dispatchers.IO)

@Database(entities = [BLEEntry::class], version = 109)
abstract class BLEDatabase : RoomDatabase() {

    abstract fun bleDao(): BLEStore

    companion object : SingletonHolder<BLEDatabase, Context>({ context ->
        val builder =
            Room.databaseBuilder(context, BLEDatabase::class.java, Utils.APPLICATION_DATABASE_NAME)

        with(builder) {
            // do not create db.bad and db.wal files, for reliable export of Room DB.
            setJournalMode(androidx.room.RoomDatabase.JournalMode.TRUNCATE)

            // HACK: Migrations will be used later, for now allow destructive migrations = destroy the database after a schema change
            fallbackToDestructiveMigration()
            // addMigrations(MIGRATION_1_2)

            addCallback(object : RoomDatabase.Callback() {

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }

                /**
                 * Pre-populate some data for testing
                 */
                private fun insertData(database: BLEDatabase) = ioScope.launch {
                }
            })

            openHelperFactory(androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory())
        }

        Log.e("BLEDatabase", "BLEDatabase initialized")
        builder.build()
    })
}
package com.example.discoverlib.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.discoverlib.data.InitialData
import com.example.discoverlib.data.local.AppDatabase
import com.example.discoverlib.data.local.dao.ActivityDao
import com.example.discoverlib.data.local.dao.TripDao
import com.example.discoverlib.data.local.dao.UserDao
import com.example.discoverlib.data.local.mapper.toEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val TAG = "DataModule"

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("discoverlib_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, provider: Provider<AppDatabase>): AppDatabase {
        return Room.databaseBuilder(context,
            AppDatabase::class.java,
            "discoverlib_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    Log.i(TAG, "Database created for the first time. Pre-populating initial data...")

                    val database = provider.get()
                    val tripDao = database.tripDao()
                    val activityDao = database.activityDao()

                    InitialData.getInitialTrips().forEach { trip ->
                        tripDao.addTrip(trip.toEntity())

                        trip.activities.forEach { activity ->
                            activityDao.addActivity(activity.toEntity(trip.id))
                        }
                    }
                    Log.i(TAG, "Initial data populated successfully!")
                }
            }
        }).build()
    }

    @Provides
    fun provideTripDao(db: AppDatabase): TripDao = db.tripDao()

    @Provides
    fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

}
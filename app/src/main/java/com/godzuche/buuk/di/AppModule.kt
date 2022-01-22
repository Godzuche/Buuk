package com.godzuche.buuk.di

import android.app.Application
import androidx.room.Room
import com.godzuche.buuk.data.BuukDatabase
import com.godzuche.buuk.data.BuukRepository
import com.godzuche.buuk.data.BuukRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesBuukDatabase(app: Application): BuukDatabase {
        return Room.databaseBuilder(
            app,
            BuukDatabase::class.java,
            "buuk_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesBuukRepository(db: BuukDatabase): BuukRepository {
        return BuukRepositoryImpl(db.userDao)
    }
}
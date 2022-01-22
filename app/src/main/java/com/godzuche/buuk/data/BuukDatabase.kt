package com.godzuche.buuk.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class BuukDatabase : RoomDatabase() {

    abstract val userDao: UserDao
}
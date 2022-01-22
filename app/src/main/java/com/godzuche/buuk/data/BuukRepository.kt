package com.godzuche.buuk.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface BuukRepository {

    fun getUser(): Flow<User?>

    suspend fun clearUser()

    suspend fun saveUser(user: User)

}
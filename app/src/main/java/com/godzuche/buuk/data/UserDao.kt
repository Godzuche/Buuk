package com.godzuche.buuk.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM profile_details_table")
    fun getUser(): Flow<User?>

    @Query("DELETE FROM profile_details_table")
    suspend fun clearUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)
}
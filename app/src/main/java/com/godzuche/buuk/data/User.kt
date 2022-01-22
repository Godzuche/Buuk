package com.godzuche.buuk.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "profile_details_table")
data class User(
    @PrimaryKey val id: Int? = null,
    val name: String?,
    val email: String?
)

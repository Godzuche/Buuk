package com.godzuche.buuk.data

import kotlinx.coroutines.flow.Flow

class BuukRepositoryImpl(
    private val userDao: UserDao
): BuukRepository {

    override fun getUser(): Flow<User?> {
        return userDao.getUser()
    }

    override suspend fun clearUser() {
        userDao.clearUser()
    }

    override suspend fun saveUser(user: User) {
        userDao.saveUser(user)
    }
}
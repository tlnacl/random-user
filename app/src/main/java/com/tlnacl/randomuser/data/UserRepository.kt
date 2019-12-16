package com.tlnacl.randomuser.data

import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject
constructor(private val api: RandomUserApi, private val appDatabase: AppDatabase) {

    suspend fun firstLoad(): List<User> {
        return appDatabase.userDao().getAll()
            .run { if (this.isEmpty()) getUsers() else return this }
    }

    suspend fun getUsers(): List<User> {
        return api.getRandomUserData().results.map { userData -> convertUser(userData) }
            .run {
                appDatabase.userDao().insertAll(this)
                appDatabase.userDao().getAll()
            }
    }

    suspend fun searchUsers(input: String): List<User>{
        val query = input.toLowerCase()
        val userResults = appDatabase.userDao().searchAllUser(query)
        Timber.d("userResults.size%s", userResults.size)
        return userResults
    }

    private fun convertUser(userData: UserData): User {
        val userName: UserName = userData.name
        val name = String.format("%s, %s", userName.first, userName.last)
        return User(
            userData.login.uuid,
            name,
            userData.gender,
            userData.dob.date.take(10),
            userData.picture.thumbnail,
            userData.picture.large
        )
    }
}
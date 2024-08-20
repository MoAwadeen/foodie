package iti.project.foodie.data.repository

import iti.project.foodie.data.source.local.User
import iti.project.foodie.data.source.local.UserDao

class AuthRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }


    suspend fun deleteUser(email: String) {
        userDao.deleteUserByEmail(email)
    }


}
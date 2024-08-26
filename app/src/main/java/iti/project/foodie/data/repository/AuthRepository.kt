package iti.project.foodie.data.repository

import iti.project.foodie.data.source.local.User
import iti.project.foodie.data.source.local.UserDao
import java.util.Date

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

    suspend fun getCurrentUserId(email: String): Int? {
        return userDao.getCurrentUserId(email)
    }

    suspend fun getCurrentUserName(email: String): String? {
        return userDao.getCurrentUserName(email)
    }

    suspend fun getCurrentUserBirthDate(email: String): String? {
        return userDao.getCurrentUserBirthDate(email)
    }

}
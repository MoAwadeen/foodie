package iti.project.foodie.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)

    @Query("SELECT id FROM users WHERE email = :email LIMIT 1")
    suspend fun getCurrentUserId(email: String): Int?

    @Query("SELECT name FROM users WHERE email = :email LIMIT 1")
    suspend fun getCurrentUserName(email: String): String?

    @Query("SELECT birthDate FROM users WHERE email = :email LIMIT 1")
    suspend fun getCurrentUserBirthDate(email: String): String?

}

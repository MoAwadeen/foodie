package iti.project.foodie.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Register_users_name")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: String,
    val email: String,
    val password: String
) {

}
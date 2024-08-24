package iti.project.foodie.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY id DESC LIMIT 5")
    suspend fun getAllSearchHistory(): List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory)

    @Query("SELECT COUNT(*) FROM search_history WHERE query = :query")
    suspend fun doesQueryExist(query: String): Int
}

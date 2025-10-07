package com.example.link

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {
    
    @Query("SELECT * FROM links ORDER BY createdAt DESC")
    fun getAllLinks(): Flow<List<Link>>
    
    @Query("SELECT * FROM links WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteLinks(): Flow<List<Link>>
    
    @Query("SELECT * FROM links WHERE tags LIKE '%' || :tag || '%' ORDER BY createdAt DESC")
    fun getLinksByTag(tag: String): Flow<List<Link>>
    
    @Query("SELECT * FROM links WHERE title LIKE '%' || :query || '%' OR url LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchLinks(query: String): Flow<List<Link>>
    
    @Query("SELECT * FROM links ORDER BY clicks DESC")
    fun getLinksByClicks(): Flow<List<Link>>
    
    @Query("SELECT * FROM links WHERE url = :url LIMIT 1")
    suspend fun getLinkByUrl(url: String): Link?
    
    @Query("SELECT * FROM links WHERE id = :id LIMIT 1")
    suspend fun getLinkById(id: Int): Link?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(link: Link): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(links: List<Link>)
    
    @Update
    suspend fun update(link: Link)
    
    @Delete
    suspend fun delete(link: Link)
    
    @Query("DELETE FROM links WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
    
    @Query("DELETE FROM links")
    suspend fun deleteAll()
    
    @Query("UPDATE links SET clicks = clicks + 1, lastAccessedAt = :timestamp WHERE id = :id")
    suspend fun incrementClicks(id: Int, timestamp: Long = System.currentTimeMillis())
    
    @Query("SELECT COUNT(*) FROM links")
    suspend fun getLinksCount(): Int
    
    @Query("SELECT COUNT(*) FROM links WHERE isFavorite = 1")
    suspend fun getFavoriteLinksCount(): Int
    
    @Query("SELECT DISTINCT tags FROM links WHERE tags != '' ORDER BY tags")
    suspend fun getAllTags(): List<String>
}
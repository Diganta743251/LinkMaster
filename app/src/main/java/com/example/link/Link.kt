package com.example.link

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "links")
@Parcelize
data class Link(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val title: String = "",
    val slug: String = "", // For lv://shortcut
    val tags: String = "", // Comma-separated, can include hierarchical like folder/subfolder
    val description: String = "",
    val notes: String = "",
    val clicks: Int = 0,
    val lastClicked: Long? = null,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastAccessedAt: Long = 0L
) : Parcelable
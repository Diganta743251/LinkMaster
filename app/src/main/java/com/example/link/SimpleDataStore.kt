package com.example.link

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object SimpleDataStore {
    private val _links = MutableStateFlow<List<Link>>(emptyList())
    val links: Flow<List<Link>> = _links.asStateFlow()
    
    suspend fun insert(link: Link) {
        val currentLinks = _links.value.toMutableList()
        currentLinks.add(link.copy(id = currentLinks.size + 1))
        _links.value = currentLinks
    }
    
    suspend fun update(link: Link) {
        val currentLinks = _links.value.toMutableList()
        val index = currentLinks.indexOfFirst { it.id == link.id }
        if (index != -1) {
            currentLinks[index] = link
            _links.value = currentLinks
        }
    }
    
    suspend fun delete(link: Link) {
        val currentLinks = _links.value.toMutableList()
        currentLinks.removeAll { it.id == link.id }
        _links.value = currentLinks
    }
    
    suspend fun deleteAll(links: List<Link>) {
        val currentLinks = _links.value.toMutableList()
        currentLinks.removeAll { link -> links.any { it.id == link.id } }
        _links.value = currentLinks
    }
    
    fun getAllLinks(): Flow<List<Link>> {
        return _links.asStateFlow()
    }
    
    fun getLinksByTag(tag: String): Flow<List<Link>> {
        return _links.map { links ->
            links.filter { it.tags.contains(tag, ignoreCase = true) }
        }
    }
    
    fun getFavoriteLinks(): Flow<List<Link>> {
        return _links.map { links ->
            links.filter { it.isFavorite }
        }
    }
    
    fun searchLinks(query: String): Flow<List<Link>> {
        return _links.map { links ->
            links.filter { 
                it.title.contains(query, ignoreCase = true) ||
                it.url.contains(query, ignoreCase = true) ||
                it.tags.contains(query, ignoreCase = true)
            }
        }
    }
    
    fun getLinksByClicks(): Flow<List<Link>> {
        return _links.map { links ->
            links.sortedByDescending { it.clicks }
        }
    }
    
    suspend fun getLinkByUrl(url: String): Link? {
        return _links.value.find { it.url == url }
    }
}
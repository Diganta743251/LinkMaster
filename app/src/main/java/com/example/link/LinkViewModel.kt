package com.example.link

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URL
import java.util.regex.Pattern
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LinkViewModel(application: Application) : AndroidViewModel(application) {
    
    private val linkDao = AppDatabase.getDatabase(application).linkDao()
    
    // Search and filter state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedTag = MutableStateFlow<String?>(null)
    val selectedTag: StateFlow<String?> = _selectedTag.asStateFlow()
    
    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()
    
    private val _sortBy = MutableStateFlow(SortBy.CREATED_DESC)
    val sortBy: StateFlow<SortBy> = _sortBy.asStateFlow()
    
    // UI state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // Links data with filtering and sorting
    val links: StateFlow<List<Link>> = combine(
        searchQuery,
        selectedTag,
        showFavoritesOnly,
        sortBy
    ) { query, tag, favoritesOnly, sort ->
        FilterCriteria(query, tag, favoritesOnly, sort)
    }.flatMapLatest { criteria ->
        getFilteredLinks(criteria)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // Statistics
    val linksCount: StateFlow<Int> = linkDao.getAllLinks()
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    
    val favoriteLinksCount: StateFlow<Int> = linkDao.getFavoriteLinks()
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    
    // Available tags
    val availableTags: StateFlow<List<String>> = linkDao.getAllLinks()
        .map { links ->
            links.flatMap { link ->
                link.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            }.distinct().sorted()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    private fun getFilteredLinks(criteria: FilterCriteria): Flow<List<Link>> {
        return when {
            criteria.query.isNotEmpty() -> linkDao.searchLinks(criteria.query)
            criteria.tag != null -> linkDao.getLinksByTag(criteria.tag)
            criteria.showFavoritesOnly -> linkDao.getFavoriteLinks()
            criteria.sortBy == SortBy.CLICKS_DESC -> linkDao.getLinksByClicks()
            else -> linkDao.getAllLinks()
        }.map { links ->
            // Apply additional filtering and sorting
            var filteredLinks = links
            
            if (criteria.showFavoritesOnly && criteria.query.isNotEmpty()) {
                filteredLinks = filteredLinks.filter { it.isFavorite }
            }
            
            // Sort if needed
            when (criteria.sortBy) {
                SortBy.CREATED_ASC -> filteredLinks.sortedBy { it.createdAt }
                SortBy.CREATED_DESC -> filteredLinks.sortedByDescending { it.createdAt }
                SortBy.TITLE_ASC -> filteredLinks.sortedBy { it.title.lowercase() }
                SortBy.TITLE_DESC -> filteredLinks.sortedByDescending { it.title.lowercase() }
                SortBy.CLICKS_DESC -> filteredLinks.sortedByDescending { it.clicks }
                SortBy.LAST_ACCESSED -> filteredLinks.sortedByDescending { it.lastAccessedAt }
            }
        }
    }
    
    // Actions
    fun insertLink(link: Link) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                linkDao.insert(link)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save link: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateLink(link: Link) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                linkDao.update(link.copy(updatedAt = System.currentTimeMillis()))
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update link: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteLink(link: Link) {
        viewModelScope.launch {
            try {
                linkDao.delete(link)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete link: ${e.message}"
            }
        }
    }
    
    fun deleteLinks(links: List<Link>) {
        viewModelScope.launch {
            try {
                linkDao.deleteByIds(links.map { it.id })
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete links: ${e.message}"
            }
        }
    }
    
    fun toggleFavorite(link: Link) {
        viewModelScope.launch {
            try {
                val updatedLink = link.copy(
                    isFavorite = !link.isFavorite,
                    updatedAt = System.currentTimeMillis()
                )
                linkDao.update(updatedLink)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update favorite: ${e.message}"
            }
        }
    }
    
    fun incrementClicks(linkId: Int) {
        viewModelScope.launch {
            try {
                linkDao.incrementClicks(linkId)
            } catch (e: Exception) {
                // Silent fail for click tracking
            }
        }
    }
    
    // Search and filter actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setSelectedTag(tag: String?) {
        _selectedTag.value = tag
    }
    
    fun toggleFavoritesOnly() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }
    
    fun setSortBy(sortBy: SortBy) {
        _sortBy.value = sortBy
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun refreshLinks() {
        // Since we're using Flow, the data will automatically refresh
        // This method can be used for manual refresh triggers
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simulate a brief loading state for user feedback
                kotlinx.coroutines.delay(500)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Utility functions
    suspend fun getLinkByUrl(url: String): Link? {
        return try {
            linkDao.getLinkByUrl(url)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getLinkById(id: Int): Link? {
        return try {
            linkDao.getLinkById(id)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun fetchTitleFromUrl(url: String): String? {
        return try {
            // Simple title extraction - in a real app, you might want to use a proper HTML parser
            val connection = URL(url).openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val html = connection.getInputStream().bufferedReader().use { it.readText() }
            
            val titlePattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
            val matcher = titlePattern.matcher(html)
            
            if (matcher.find()) {
                matcher.group(1)?.trim()?.take(100) // Limit title length
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    // Export/Import functionality
    suspend fun exportLinksToJson(): String {
        return try {
            val allLinks = linkDao.getAllLinks().first()
            Gson().toJson(allLinks)
        } catch (e: Exception) {
            throw Exception("Failed to export links: ${e.message}")
        }
    }
    
    suspend fun importLinksFromJson(jsonString: String): Int {
        return try {
            val type = object : TypeToken<List<Link>>() {}.type
            val importedLinks: List<Link> = Gson().fromJson(jsonString, type)
            
            var importedCount = 0
            importedLinks.forEach { link ->
                // Check if link already exists
                val existingLink = linkDao.getLinkByUrl(link.url)
                if (existingLink == null) {
                    linkDao.insert(link.copy(id = 0)) // Reset ID for new insertion
                    importedCount++
                }
            }
            importedCount
        } catch (e: Exception) {
            throw Exception("Failed to import links: ${e.message}")
        }
    }
    
    // Backup functionality
    suspend fun createBackup(): String {
        return exportLinksToJson()
    }
    
    suspend fun restoreFromBackup(backupData: String): Int {
        return importLinksFromJson(backupData)
    }
    
    // Android-specific import/export methods
    fun exportLinks(context: android.content.Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val importExportManager = ImportExportManager()
                importExportManager.exportLinks(context, linkDao.getAllLinks().first())
                _errorMessage.value = "Links exported successfully"
            } catch (e: Exception) {
                _errorMessage.value = "Export failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun importLinks(context: android.content.Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val importExportManager = ImportExportManager()
                importExportManager.importLinks(context) { importedLinks ->
                    viewModelScope.launch {
                        try {
                            var importedCount = 0
                            importedLinks.forEach { link ->
                                val existingLink = linkDao.getLinkByUrl(link.url)
                                if (existingLink == null) {
                                    linkDao.insert(link.copy(id = 0))
                                    importedCount++
                                }
                            }
                            _errorMessage.value = "Successfully imported $importedCount links"
                        } catch (e: Exception) {
                            _errorMessage.value = "Import failed: ${e.message}"
                        } finally {
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Import failed: ${e.message}"
                _isLoading.value = false
            }
        }
    }
}

data class FilterCriteria(
    val query: String,
    val tag: String?,
    val showFavoritesOnly: Boolean,
    val sortBy: SortBy
)

enum class SortBy {
    CREATED_ASC,
    CREATED_DESC,
    TITLE_ASC,
    TITLE_DESC,
    CLICKS_DESC,
    LAST_ACCESSED
}
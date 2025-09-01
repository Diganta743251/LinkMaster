package fe.linksheet.module.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fe.linksheet.composable.page.history.HistoryItem
import fe.linksheet.composable.page.history.HistoryViewMode
import fe.linksheet.composable.page.history.HistorySortMode
import fe.linksheet.module.database.entity.AppSelectionHistory
import fe.linksheet.module.repository.AppSelectionHistoryRepository
import fe.linksheet.security.DataProtection
import fe.linksheet.security.sanitizeForLog
import fe.linksheet.security.isSecureUrl
import fe.linksheet.util.PerformanceMonitor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for History Screen with modern state management
 */
class HistoryViewModel(
    private val context: Context,
    private val appSelectionHistoryRepository: AppSelectionHistoryRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    
    private val _viewMode = MutableStateFlow(HistoryViewMode.LIST)
    val viewMode = _viewMode.asStateFlow()
    
    private val _sortMode = MutableStateFlow(HistorySortMode.RECENT)
    val sortMode = _sortMode.asStateFlow()
    
    private val _selectedItems = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItems = _selectedItems.asStateFlow()
    
    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode = _isSelectionMode.asStateFlow()
    
    private val _historyData = MutableStateFlow<List<AppSelectionHistory>>(emptyList())
    
    // Transform raw history data to UI-friendly format
    val historyItems = _historyData.map { historyList ->
        historyList.map { history ->
            HistoryItem(
                id = history.id,
                host = history.host,
                packageName = history.packageName,
                appName = getAppName(history.packageName),
                lastUsed = history.lastUsed,
                isPinned = false, // TODO: Implement pinned functionality
                isStarred = false, // TODO: Implement starred functionality
                visitCount = 1 // TODO: Calculate actual visit count
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    init {
        loadHistoryData()
    }
    
    private fun loadHistoryData() {
        viewModelScope.launch {
            // TODO: Implement actual data loading from repository
            // For now, using mock data
            _historyData.value = generateMockHistoryData()
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun updateViewMode(mode: HistoryViewMode) {
        _viewMode.value = mode
    }
    
    fun updateSortMode(mode: HistorySortMode) {
        _sortMode.value = mode
    }
    
    fun onItemClick(item: HistoryItem) {
        if (_isSelectionMode.value) {
            toggleSelection(item.id)
        } else {
            // Handle normal click - navigate to link detail screen
            navigateToLinkDetail(item.host)
        }
    }
    
    private fun navigateToLinkDetail(host: String) {
        // This will be handled by the navigation system
        // The URL will be passed to LinkDetailScreen
        val fullUrl = if (host.startsWith("http")) host else "https://$host"
        // Navigation will be handled in the composable
    }
    
    fun onItemLongClick(item: HistoryItem) {
        if (!_isSelectionMode.value) {
            _isSelectionMode.value = true
        }
        toggleSelection(item.id)
    }
    
    fun onSelectionChanged(itemId: Int, selected: Boolean) {
        val currentSelection = _selectedItems.value.toMutableSet()
        if (selected) {
            currentSelection.add(itemId)
        } else {
            currentSelection.remove(itemId)
        }
        _selectedItems.value = currentSelection
        
        if (currentSelection.isEmpty()) {
            _isSelectionMode.value = false
        }
    }
    
    private fun toggleSelection(itemId: Int) {
        val currentSelection = _selectedItems.value.toMutableSet()
        if (currentSelection.contains(itemId)) {
            currentSelection.remove(itemId)
        } else {
            currentSelection.add(itemId)
        }
        _selectedItems.value = currentSelection
        
        if (currentSelection.isEmpty()) {
            _isSelectionMode.value = false
        }
    }
    
    fun clearSelection() {
        _selectedItems.value = emptySet()
        _isSelectionMode.value = false
    }
    
    fun deleteSelectedItems() {
        viewModelScope.launch {
            val selectedIds = _selectedItems.value
            val currentItems = _historyData.value.toMutableList()
            
            // Remove selected items
            currentItems.removeAll { it.id in selectedIds }
            _historyData.value = currentItems
            
            // TODO: Delete from database
            // appSelectionHistoryRepository.deleteByIds(selectedIds.toList())
            
            clearSelection()
        }
    }
    
    fun pinSelectedItems() {
        viewModelScope.launch {
            // TODO: Implement pin functionality
            // For now, just clear selection
            clearSelection()
        }
    }
    
    fun starSelectedItems() {
        viewModelScope.launch {
            // TODO: Implement star functionality
            // For now, just clear selection
            clearSelection()
        }
    }
    
    fun shareSelectedItems() {
        val selectedIds = _selectedItems.value
        val selectedItems = _historyData.value.filter { it.id in selectedIds }
        
        if (selectedItems.isNotEmpty()) {
            val shareText = selectedItems.joinToString("\n") { "https://${it.host}" }
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            
            val chooserIntent = Intent.createChooser(shareIntent, "Share links")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        }
        
        clearSelection()
    }
    
    private fun openLink(host: String, packageName: String) {
        // Security: Validate URL before opening
        val fullUrl = if (host.startsWith("http")) host else "https://$host"
        if (!fullUrl.isSecureUrl()) {
            Log.w("HistoryViewModel", "Blocked unsafe URL: ${fullUrl.sanitizeForLog()}")
            return
        }
        
        // TODO: Implement link opening logic
        // This would typically create an intent to open the link with the specified app
        Log.d("HistoryViewModel", "Opening secure URL: ${fullUrl.sanitizeForLog()}")
    }
    
    private fun getAppName(packageName: String): String {
        // TODO: Get actual app name from package manager
        // For now, return a formatted package name
        return packageName.split(".").lastOrNull()?.replaceFirstChar { it.uppercase() } ?: packageName
    }
    
    // Security Methods
    
    /**
     * Validates and sanitizes history data before processing
     */
    private fun validateHistoryData(data: List<AppSelectionHistory>): List<AppSelectionHistory> {
        return data.filter { item ->
            // Validate host is not empty and looks like a valid domain
            item.host.isNotBlank() && 
            item.host.contains(".") && 
            !item.host.contains("localhost") &&
            !item.host.startsWith("127.") &&
            !item.host.startsWith("192.168.") &&
            item.packageName.isNotBlank()
        }
    }
    
    /**
     * Securely logs history operations without exposing sensitive data
     */
    private fun logHistoryOperation(operation: String, itemCount: Int = 0, additionalInfo: String = "") {
        val sanitizedInfo = if (additionalInfo.isNotBlank()) {
            additionalInfo.sanitizeForLog()
        } else ""
        
        Log.d("HistoryViewModel", "History operation: $operation, items: $itemCount $sanitizedInfo")
    }
    
    /**
     * Performs secure cleanup of sensitive data
     */
    fun performSecureCleanup() {
        viewModelScope.launch {
            try {
                // Clear sensitive data from memory
                DataProtection.clearSensitiveData(context)
                
                // Clear any temporary data
                _searchQuery.value = ""
                clearSelection()
                
                logHistoryOperation("Secure cleanup completed")
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Secure cleanup failed", e)
            }
        }
    }
    
    /**
     * Validates app integrity before performing sensitive operations
     */
    private fun validateAppIntegrity(): Boolean {
        return DataProtection.validateAppIntegrity(context)
    }
    
    override fun onCleared() {
        super.onCleared()
        // Perform secure cleanup when ViewModel is destroyed
        performSecureCleanup()
    }
    
    private fun generateMockHistoryData(): List<AppSelectionHistory> {
        val currentTime = System.currentTimeMillis()
        val oneHour = 60 * 60 * 1000L
        val oneDay = 24 * oneHour
        
        return listOf(
            AppSelectionHistory(1, "github.com", "com.github.android", currentTime - oneHour),
            AppSelectionHistory(2, "stackoverflow.com", "com.chrome.android", currentTime - 2 * oneHour),
            AppSelectionHistory(3, "reddit.com", "com.reddit.frontpage", currentTime - 3 * oneHour),
            AppSelectionHistory(4, "youtube.com", "com.google.android.youtube", currentTime - 4 * oneHour),
            AppSelectionHistory(5, "twitter.com", "com.twitter.android", currentTime - oneDay),
            AppSelectionHistory(6, "medium.com", "com.chrome.android", currentTime - oneDay - oneHour),
            AppSelectionHistory(7, "dev.to", "com.chrome.android", currentTime - oneDay - 2 * oneHour),
            AppSelectionHistory(8, "google.com", "com.chrome.android", currentTime - 2 * oneDay),
            AppSelectionHistory(9, "wikipedia.org", "com.chrome.android", currentTime - 2 * oneDay - oneHour),
            AppSelectionHistory(10, "news.ycombinator.com", "com.chrome.android", currentTime - 3 * oneDay),
            AppSelectionHistory(11, "linkedin.com", "com.linkedin.android", currentTime - 3 * oneDay - oneHour),
            AppSelectionHistory(12, "instagram.com", "com.instagram.android", currentTime - 4 * oneDay),
            AppSelectionHistory(13, "facebook.com", "com.facebook.katana", currentTime - 4 * oneDay - oneHour),
            AppSelectionHistory(14, "amazon.com", "com.amazon.mShop.android.shopping", currentTime - 5 * oneDay),
            AppSelectionHistory(15, "netflix.com", "com.netflix.mediaclient", currentTime - 5 * oneDay - oneHour)
        )
    }
}
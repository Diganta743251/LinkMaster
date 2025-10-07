package com.example.link

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ImportExportManager {
    
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()
    
    data class ExportData(
        val version: String = "1.0",
        val exportDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
        val appName: String = "LinkVault",
        val totalLinks: Int,
        val links: List<Link>
    )
    
    suspend fun exportToJson(
        context: Context,
        links: List<Link>,
        outputUri: Uri
    ): ExportResult = withContext(Dispatchers.IO) {
        try {
            val exportData = ExportData(
                totalLinks = links.size,
                links = links
            )
            
            context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                OutputStreamWriter(outputStream, "UTF-8").use { writer ->
                    gson.toJson(exportData, writer)
                }
            }
            
            ExportResult.Success(links.size)
        } catch (e: Exception) {
            ExportResult.Error("Failed to export: ${e.message}")
        }
    }
    
    suspend fun importFromJson(context: Context, inputUri: Uri): ImportResult = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(inputUri)?.use { inputStream ->
                InputStreamReader(inputStream, "UTF-8").use { reader ->
                    val exportData = gson.fromJson(reader, ExportData::class.java)
                    
                    // Validate the data
                    if (exportData.links.isEmpty()) {
                        return@withContext ImportResult.Error("No links found in the file")
                    }
                    
                    // Sanitize and validate each link
                    val validLinks = exportData.links.mapNotNull { link ->
                        if (isValidLink(link)) {
                            link.copy(
                                id = 0, // Reset ID for new insertion
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                        } else null
                    }
                    
                    if (validLinks.isEmpty()) {
                        return@withContext ImportResult.Error("No valid links found in the file")
                    }
                    
                    ImportResult.Success(validLinks, exportData.totalLinks)
                }
            } ?: ImportResult.Error("Could not read the file")
        } catch (e: Exception) {
            ImportResult.Error("Failed to import: ${e.message}")
        }
    }
    
    suspend fun exportToCsv(
        context: Context,
        links: List<Link>,
        outputUri: Uri
    ): ExportResult = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                OutputStreamWriter(outputStream, "UTF-8").use { writer ->
                    // Write CSV header
                    writer.write("URL,Title,Tags,Description,Notes,Clicks,IsFavorite,CreatedAt,UpdatedAt\n")
                    
                    // Write data rows
                    links.forEach { link ->
                        val row = listOf(
                            escapeCsvField(link.url),
                            escapeCsvField(link.title),
                            escapeCsvField(link.tags),
                            escapeCsvField(link.description),
                            escapeCsvField(link.notes),
                            link.clicks.toString(),
                            link.isFavorite.toString(),
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(link.createdAt)),
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(link.updatedAt))
                        ).joinToString(",")
                        
                        writer.write("$row\n")
                    }
                }
            }
            
            ExportResult.Success(links.size)
        } catch (e: Exception) {
            ExportResult.Error("Failed to export CSV: ${e.message}")
        }
    }
    
    suspend fun importFromCsv(context: Context, inputUri: Uri): ImportResult = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(inputUri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                    val lines = reader.readLines()
                    
                    if (lines.isEmpty()) {
                        return@withContext ImportResult.Error("File is empty")
                    }
                    
                    // Skip header
                    val dataLines = lines.drop(1)
                    val links = mutableListOf<Link>()
                    
                    dataLines.forEachIndexed { index, line ->
                        try {
                            val fields = parseCsvLine(line)
                            if (fields.size >= 5) { // Minimum required fields
                                val link = Link(
                                    url = fields[0],
                                    title = fields.getOrElse(1) { "" },
                                    tags = fields.getOrElse(2) { "" },
                                    description = fields.getOrElse(3) { "" },
                                    notes = fields.getOrElse(4) { "" },
                                    clicks = fields.getOrElse(5) { "0" }.toIntOrNull() ?: 0,
                                    isFavorite = fields.getOrElse(6) { "false" }.toBooleanStrictOrNull() ?: false,
                                    createdAt = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                
                                if (isValidLink(link)) {
                                    links.add(link)
                                }
                            }
                        } catch (e: Exception) {
                            // Skip invalid lines
                        }
                    }
                    
                    if (links.isEmpty()) {
                        ImportResult.Error("No valid links found in CSV")
                    } else {
                        ImportResult.Success(links, dataLines.size)
                    }
                }
            } ?: ImportResult.Error("Could not read the CSV file")
        } catch (e: Exception) {
            ImportResult.Error("Failed to import CSV: ${e.message}")
        }
    }
    
    private fun isValidLink(link: Link): Boolean {
        return link.url.isNotBlank() && 
               (link.url.startsWith("http://") || link.url.startsWith("https://") || link.url.startsWith("ftp://"))
    }
    
    private fun escapeCsvField(field: String): String {
        return if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            "\"${field.replace("\"", "\"\"")}\"" 
        } else {
            field
        }
    }
    
    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var i = 0
        
        while (i < line.length) {
            val char = line[i]
            when {
                char == '"' && !inQuotes -> inQuotes = true
                char == '"' && inQuotes -> {
                    if (i + 1 < line.length && line[i + 1] == '"') {
                        current.append('"')
                        i++ // Skip next quote
                    } else {
                        inQuotes = false
                    }
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current = StringBuilder()
                }
                else -> current.append(char)
            }
            i++
        }
        
        result.add(current.toString())
        return result
    }
    
    // Android-specific convenience methods
    suspend fun exportLinks(context: Context, links: List<Link>) {
        try {
            val fileName = "links_export_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.json"
            val exportData = ExportData(
                totalLinks = links.size,
                links = links
            )
            
            // Save to external files directory
            val file = File(context.getExternalFilesDir(null), fileName)
            file.writeText(gson.toJson(exportData))
            
            // You could also implement sharing via Intent here
            shareFile(context, file)
        } catch (e: Exception) {
            throw Exception("Export failed: ${e.message}")
        }
    }
    
    suspend fun importLinks(context: Context, onResult: (List<Link>) -> Unit) {
        // This would typically open a file picker
        // For now, we'll implement a basic version
        try {
            // Implementation would involve file picker and reading
            // For demo purposes, we'll create an empty result
            onResult(emptyList())
        } catch (e: Exception) {
            throw Exception("Import failed: ${e.message}")
        }
    }
    
    fun exportSettings(context: Context) {
        try {
            val settings = mapOf(
                "alwaysAsk" to context.getSharedPreferences("linksheet", Context.MODE_PRIVATE)
                    .getBoolean("always_ask", false),
                "saveBeforeOpen" to context.getSharedPreferences("linksheet", Context.MODE_PRIVATE)
                    .getBoolean("save_before_open", true)
            )
            
            val fileName = "linksheet_settings_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.json"
            val file = File(context.getExternalFilesDir(null), fileName)
            file.writeText(gson.toJson(settings))
            
            shareFile(context, file)
        } catch (e: Exception) {
            throw Exception("Settings export failed: ${e.message}")
        }
    }
    
    fun importSettings(context: Context, onResult: (Boolean) -> Unit) {
        try {
            // Implementation would involve file picker and reading settings
            // For demo purposes, we'll return success
            onResult(true)
        } catch (e: Exception) {
            onResult(false)
        }
    }
    
    private fun shareFile(context: Context, file: File) {
        try {
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(android.content.Intent.EXTRA_STREAM, androidx.core.content.FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                ))
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(android.content.Intent.createChooser(intent, "Share Export"))
        } catch (e: Exception) {
            // Fallback: just save the file
        }
    }
}

sealed class ExportResult {
    data class Success(val exportedCount: Int) : ExportResult()
    data class Error(val message: String) : ExportResult()
}

sealed class ImportResult {
    data class Success(val links: List<Link>, val totalProcessed: Int) : ImportResult()
    data class Error(val message: String) : ImportResult()
}
package com.example.link.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.link.Link
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImportExportManager {
    
    fun exportToJson(links: List<Link>): String {
        val jsonArray = JSONArray()
        
        links.forEach { link ->
            val jsonObject = JSONObject().apply {
                put("id", link.id)
                put("url", link.url)
                put("title", link.title)
                put("slug", link.slug)
                put("tags", link.tags)
                put("description", link.description)
                put("notes", link.notes)
                put("isFavorite", link.isFavorite)
                put("clicks", link.clicks)
                put("createdAt", link.createdAt)
                put("updatedAt", link.updatedAt)
                put("lastAccessedAt", link.lastAccessedAt)
            }
            jsonArray.put(jsonObject)
        }
        
        val exportData = JSONObject().apply {
            put("version", "1.0")
            put("exportedAt", System.currentTimeMillis())
            put("totalLinks", links.size)
            put("links", jsonArray)
        }
        
        return exportData.toString(2) // Pretty print with 2 spaces
    }
    
    fun exportToCsv(links: List<Link>): String {
        val csv = StringBuilder()
        
        // Header
        csv.append("ID,URL,Title,Slug,Tags,Description,Notes,IsFavorite,Clicks,CreatedAt,UpdatedAt,LastAccessedAt\n")
        
        // Data rows
        links.forEach { link ->
            csv.append("${link.id},")
            csv.append("\"${escapeCsv(link.url)}\",")
            csv.append("\"${escapeCsv(link.title)}\",")
            csv.append("\"${escapeCsv(link.slug)}\",")
            csv.append("\"${escapeCsv(link.tags)}\",")
            csv.append("\"${escapeCsv(link.description)}\",")
            csv.append("\"${escapeCsv(link.notes)}\",")
            csv.append("${link.isFavorite},")
            csv.append("${link.clicks},")
            csv.append("${link.createdAt},")
            csv.append("${link.updatedAt},")
            csv.append("${link.lastAccessedAt}\n")
        }
        
        return csv.toString()
    }
    
    fun exportToHtml(links: List<Link>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val exportDate = dateFormat.format(Date())
        
        val html = StringBuilder()
        html.append("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>LinkVault Export - $exportDate</title>
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 1200px;
                        margin: 0 auto;
                        padding: 20px;
                        background: #f8f9fa;
                    }
                    .header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 30px;
                        border-radius: 10px;
                        margin-bottom: 30px;
                        text-align: center;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 2.5em;
                        font-weight: 300;
                    }
                    .header .subtitle {
                        margin: 10px 0 0 0;
                        opacity: 0.9;
                        font-size: 1.1em;
                    }
                    .stats {
                        display: flex;
                        justify-content: space-around;
                        margin-bottom: 30px;
                        gap: 20px;
                    }
                    .stat-card {
                        background: white;
                        padding: 20px;
                        border-radius: 10px;
                        text-align: center;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                        flex: 1;
                    }
                    .stat-number {
                        font-size: 2em;
                        font-weight: bold;
                        color: #667eea;
                        margin-bottom: 5px;
                    }
                    .stat-label {
                        color: #666;
                        font-size: 0.9em;
                    }
                    .links-container {
                        background: white;
                        border-radius: 10px;
                        overflow: hidden;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    .link-item {
                        padding: 20px;
                        border-bottom: 1px solid #eee;
                        transition: background-color 0.2s;
                    }
                    .link-item:hover {
                        background-color: #f8f9fa;
                    }
                    .link-item:last-child {
                        border-bottom: none;
                    }
                    .link-title {
                        font-size: 1.2em;
                        font-weight: 600;
                        margin-bottom: 8px;
                    }
                    .link-title a {
                        color: #667eea;
                        text-decoration: none;
                    }
                    .link-title a:hover {
                        text-decoration: underline;
                    }
                    .link-url {
                        color: #666;
                        font-size: 0.9em;
                        margin-bottom: 8px;
                        word-break: break-all;
                    }
                    .link-description {
                        color: #555;
                        margin-bottom: 10px;
                        line-height: 1.5;
                    }
                    .link-notes {
                        background: #f8f9fa;
                        padding: 10px;
                        border-radius: 5px;
                        font-style: italic;
                        color: #666;
                        margin-bottom: 10px;
                    }
                    .link-meta {
                        display: flex;
                        flex-wrap: wrap;
                        gap: 15px;
                        font-size: 0.85em;
                        color: #666;
                    }
                    .tags {
                        display: flex;
                        flex-wrap: wrap;
                        gap: 5px;
                        margin-bottom: 10px;
                    }
                    .tag {
                        background: #e9ecef;
                        color: #495057;
                        padding: 3px 8px;
                        border-radius: 12px;
                        font-size: 0.8em;
                    }
                    .favorite {
                        color: #ffc107;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 40px;
                        padding: 20px;
                        color: #666;
                        font-size: 0.9em;
                    }
                    @media (max-width: 768px) {
                        .stats {
                            flex-direction: column;
                        }
                        .link-meta {
                            flex-direction: column;
                            gap: 5px;
                        }
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>LinkVault Export</h1>
                    <div class="subtitle">Exported on $exportDate</div>
                </div>
                
                <div class="stats">
                    <div class="stat-card">
                        <div class="stat-number">${links.size}</div>
                        <div class="stat-label">Total Links</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-number">${links.count { it.isFavorite }}</div>
                        <div class="stat-label">Favorites</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-number">${links.sumOf { it.clicks }}</div>
                        <div class="stat-label">Total Clicks</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-number">${links.flatMap { it.tags.split(",") }.map { it.trim() }.filter { it.isNotEmpty() }.toSet().size}</div>
                        <div class="stat-label">Unique Tags</div>
                    </div>
                </div>
                
                <div class="links-container">
        """.trimIndent())
        
        links.sortedByDescending { it.createdAt }.forEach { link ->
            val createdDate = dateFormat.format(Date(link.createdAt))
            val tags = link.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            
            html.append("""
                <div class="link-item">
                    <div class="link-title">
                        <a href="${escapeHtml(link.url)}" target="_blank" rel="noopener noreferrer">
                            ${escapeHtml(link.title)}
                        </a>
                        ${if (link.isFavorite) "<span class=\"favorite\">★</span>" else ""}
                    </div>
                    <div class="link-url">${escapeHtml(link.url)}</div>
                    ${if (link.description.isNotEmpty()) "<div class=\"link-description\">${escapeHtml(link.description)}</div>" else ""}
                    ${if (link.notes.isNotEmpty()) "<div class=\"link-notes\">${escapeHtml(link.notes)}</div>" else ""}
                    ${if (tags.isNotEmpty()) {
                        "<div class=\"tags\">" + tags.joinToString("") { "<span class=\"tag\">${escapeHtml(it)}</span>" } + "</div>"
                    } else ""}
                    <div class="link-meta">
                        <span>Created: $createdDate</span>
                        <span>Clicks: ${link.clicks}</span>
                        ${if (link.slug.isNotEmpty()) "<span>Slug: ${escapeHtml(link.slug)}</span>" else ""}
                    </div>
                </div>
            """.trimIndent())
        }
        
        html.append("""
                </div>
                
                <div class="footer">
                    <p>Generated by LinkVault - Your Personal Link Manager</p>
                    <p>This export contains ${links.size} links from your collection.</p>
                </div>
            </body>
            </html>
        """.trimIndent())
        
        return html.toString()
    }
    
    fun saveToFile(context: Context, content: String, filename: String, mimeType: String): Uri? {
        return try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            
            val file = File(downloadsDir, filename)
            FileOutputStream(file).use { fos ->
                fos.write(content.toByteArray())
            }
            
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    
    fun importFromJson(jsonString: String): List<Link> {
        val links = mutableListOf<Link>()
        
        try {
            val jsonObject = JSONObject(jsonString)
            val linksArray = jsonObject.getJSONArray("links")
            
            for (i in 0 until linksArray.length()) {
                val linkJson = linksArray.getJSONObject(i)
                
                val link = Link(
                    id = linkJson.optInt("id", 0),
                    url = linkJson.getString("url"),
                    title = linkJson.getString("title"),
                    slug = linkJson.optString("slug", ""),
                    tags = linkJson.optString("tags", ""),
                    description = linkJson.optString("description", ""),
                    notes = linkJson.optString("notes", ""),
                    isFavorite = linkJson.optBoolean("isFavorite", false),
                    clicks = linkJson.optInt("clicks", 0),
                    createdAt = linkJson.optLong("createdAt", System.currentTimeMillis()),
                    updatedAt = linkJson.optLong("updatedAt", System.currentTimeMillis()),
                    lastAccessedAt = linkJson.optLong("lastAccessedAt", 0)
                )
                
                links.add(link)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return links
    }
    
    fun importFromCsv(csvString: String): List<Link> {
        val links = mutableListOf<Link>()
        
        try {
            val lines = csvString.split("\n")
            if (lines.isEmpty()) return links
            
            // Skip header line
            for (i in 1 until lines.size) {
                val line = lines[i].trim()
                if (line.isEmpty()) continue
                
                val columns = parseCsvLine(line)
                if (columns.size >= 12) {
                    val link = Link(
                        id = columns[0].toIntOrNull() ?: 0,
                        url = columns[1],
                        title = columns[2],
                        slug = columns[3],
                        tags = columns[4],
                        description = columns[5],
                        notes = columns[6],
                        isFavorite = columns[7].toBoolean(),
                        clicks = columns[8].toIntOrNull() ?: 0,
                        createdAt = columns[9].toLongOrNull() ?: System.currentTimeMillis(),
                        updatedAt = columns[10].toLongOrNull() ?: System.currentTimeMillis(),
                        lastAccessedAt = columns[11].toLongOrNull() ?: 0
                    )
                    
                    links.add(link)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return links
    }
    
    private fun escapeCsv(text: String): String {
        return text.replace("\"", "\"\"")
    }
    
    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
    
    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var i = 0
        
        while (i < line.length) {
            val char = line[i]
            
            when {
                char == '"' && inQuotes && i + 1 < line.length && line[i + 1] == '"' -> {
                    // Escaped quote
                    current.append('"')
                    i++ // Skip next quote
                }
                char == '"' -> {
                    inQuotes = !inQuotes
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current = StringBuilder()
                }
                else -> {
                    current.append(char)
                }
            }
            i++
        }
        
        result.add(current.toString())
        return result
    }
}
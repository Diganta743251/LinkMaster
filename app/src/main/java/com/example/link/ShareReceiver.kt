package com.example.link

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.link.utils.SmartCategorizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                handleSharedContent(context, intent)
            }
        }
    }
    
    private fun handleSharedContent(context: Context, intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        val sharedSubject = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        
        if (sharedText != null && (sharedText.startsWith("http://") || sharedText.startsWith("https://"))) {
            // It's a URL, save it directly
            saveSharedLink(context, sharedText, sharedSubject ?: "")
        } else {
            // Open the main app to handle the content
            val mainIntent = Intent(context, MainActivity::class.java).apply {
                putExtra(Intent.EXTRA_TEXT, sharedText)
                putExtra(Intent.EXTRA_SUBJECT, sharedSubject)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(mainIntent)
        }
    }
    
    private fun saveSharedLink(context: Context, url: String, title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = AppDatabase.getDatabase(context)
                val linkDao = database.linkDao()
                
                // Check if link already exists
                val existingLink = linkDao.getLinkByUrl(url)
                if (existingLink != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Link already exists in LinkVault", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                
                // Categorize the link
                val category = SmartCategorizer.categorizeLink(url, title)
                
                // Create new link
                val link = Link(
                    url = url,
                    title = title.ifEmpty { "Shared Link" },
                    tags = category.suggestedTags.joinToString(", "),
                    description = "",
                    notes = "Shared to LinkVault",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                
                linkDao.insert(link)
                
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Link saved to LinkVault", Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error saving link: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
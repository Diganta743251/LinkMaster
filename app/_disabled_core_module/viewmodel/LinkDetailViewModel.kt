package fe.linksheet.module.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.net.URL

/**
 * ViewModel for Link Detail Screen with auto-generated summary functionality
 */
class LinkDetailViewModel(
    private val context: Context,
    private val linkUrl: String
) : ViewModel() {
    
    private val _linkSummary = MutableStateFlow<String?>(null)
    val linkSummary = _linkSummary.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    /**
     * Generate auto-summary for the link
     * This is a mock implementation - in a real app, you would use AI/ML services
     */
    fun generateSummary() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Simulate network delay
                delay(2000)
                
                // Generate mock summary based on URL
                val summary = generateMockSummary(linkUrl)
                _linkSummary.value = summary
                
            } catch (e: Exception) {
                _error.value = "Failed to generate summary: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Copy text to clipboard
     */
    fun copyToClipboard(text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Link URL", text)
        clipboardManager.setPrimaryClip(clipData)
        
        Toast.makeText(context, "URL copied to clipboard", Toast.LENGTH_SHORT).show()
    }
    
    /**
     * Open link in browser or appropriate app
     */
    fun openLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to open link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Generate mock summary based on URL domain and path
     * In a real implementation, this would use AI/ML services like:
     * - OpenAI GPT API
     * - Google Cloud Natural Language API
     * - Custom ML model
     * - Web scraping + summarization
     */
    private fun generateMockSummary(url: String): String {
        return try {
            val uri = URL(url)
            val domain = uri.host.lowercase()
            val path = uri.path.lowercase()
            
            when {
                domain.contains("github") -> {
                    "🔧 GitHub Repository\n\nThis appears to be a GitHub repository or project page. GitHub is a platform for version control and collaboration, allowing developers to work together on projects from anywhere. The link likely contains source code, documentation, or project information that can be useful for development purposes."
                }
                domain.contains("stackoverflow") -> {
                    "❓ Stack Overflow Question\n\nThis is a Stack Overflow page, the world's largest community for developers to learn and share programming knowledge. The link likely contains a programming question with community-provided answers and solutions. Stack Overflow is an excellent resource for troubleshooting code issues and learning best practices."
                }
                domain.contains("youtube") -> {
                    "🎥 YouTube Video\n\nThis link leads to a YouTube video. YouTube is the world's largest video sharing platform where users can watch, share, and upload videos on virtually any topic. The content could be educational, entertainment, music, tutorials, or any other type of video content."
                }
                domain.contains("reddit") -> {
                    "💬 Reddit Discussion\n\nThis is a Reddit link, leading to a discussion thread on one of the many subreddit communities. Reddit is known as 'the front page of the internet' where users share content, engage in discussions, and vote on posts. The link likely contains community discussions, news, or user-generated content."
                }
                domain.contains("twitter") || domain.contains("x.com") -> {
                    "🐦 Social Media Post\n\nThis link leads to a social media post, likely containing short-form content, news updates, or social interactions. Social media platforms are great for staying updated with current events, following interesting people, and engaging in real-time conversations."
                }
                domain.contains("medium") -> {
                    "📝 Medium Article\n\nThis is a Medium article link. Medium is a popular publishing platform where writers share in-depth articles on various topics including technology, business, personal development, and more. The content is typically well-researched and provides valuable insights on the subject matter."
                }
                domain.contains("wikipedia") -> {
                    "📚 Wikipedia Article\n\nThis link leads to a Wikipedia article, the world's largest free encyclopedia. Wikipedia provides comprehensive, crowd-sourced information on virtually any topic. The article likely contains detailed, factual information with references and citations for further reading."
                }
                domain.contains("news") || domain.contains("cnn") || domain.contains("bbc") || domain.contains("reuters") -> {
                    "📰 News Article\n\nThis appears to be a news article from a reputable news source. News articles provide current information about events, politics, business, technology, and other topics of public interest. The content is typically fact-checked and written by professional journalists."
                }
                domain.contains("amazon") -> {
                    "🛒 E-commerce Product\n\nThis link leads to an e-commerce platform, likely showing a product listing or shopping page. E-commerce sites allow users to browse, compare, and purchase products online. The link might contain product details, reviews, pricing, and purchasing options."
                }
                domain.contains("google") -> {
                    "🔍 Google Service\n\nThis is a link to a Google service or search result. Google provides various services including search, maps, documents, cloud storage, and more. The link could lead to search results, Google Drive files, Maps locations, or other Google services."
                }
                path.contains("blog") || path.contains("article") -> {
                    "📖 Blog Post or Article\n\nThis link appears to lead to a blog post or article. Blog content typically provides insights, tutorials, opinions, or information on specific topics. The content could be educational, informational, or entertainment-focused depending on the blog's niche."
                }
                path.contains("video") || path.contains("watch") -> {
                    "🎬 Video Content\n\nThis link leads to video content. Videos can be educational, entertaining, informational, or promotional. The content might include tutorials, documentaries, entertainment shows, or user-generated content depending on the platform and creator."
                }
                else -> {
                    "🌐 Web Content\n\nThis link leads to web content on ${domain}. The page likely contains information, services, or resources related to the website's purpose. Web content can vary widely from informational articles and business services to entertainment and educational resources.\n\nDomain: ${domain}\nThe specific content and purpose would depend on the website's focus and the particular page being linked to."
                }
            }
        } catch (e: Exception) {
            "🌐 Web Link\n\nThis appears to be a web link to online content. The specific nature of the content cannot be determined automatically, but it likely contains information, services, or resources available on the internet. You can click 'Open' to view the actual content in your browser or preferred app."
        }
    }
}
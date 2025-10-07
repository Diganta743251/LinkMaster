package com.example.link.utils

import android.content.Context
import android.net.Uri
import java.util.regex.Pattern

data class LinkCategory(
    val name: String,
    val icon: String,
    val color: String,
    val confidence: Float,
    val suggestedTags: List<String> = emptyList()
)

data class AppSuggestion(
    val packageName: String,
    val appName: String,
    val confidence: Float,
    val reason: String
)

object SmartCategorizer {
    
    // Domain patterns for different categories
    private val categoryPatterns = mapOf(
        "Social Media" to listOf(
            "facebook.com", "twitter.com", "instagram.com", "linkedin.com", 
            "tiktok.com", "snapchat.com", "pinterest.com", "reddit.com",
            "discord.com", "telegram.org", "whatsapp.com", "x.com"
        ),
        "Video Streaming" to listOf(
            "youtube.com", "netflix.com", "hulu.com", "disney", "amazon.com/prime",
            "twitch.tv", "vimeo.com", "dailymotion.com", "crunchyroll.com"
        ),
        "Music" to listOf(
            "spotify.com", "apple.com/music", "soundcloud.com", "pandora.com",
            "deezer.com", "tidal.com", "bandcamp.com", "last.fm"
        ),
        "News & Media" to listOf(
            "cnn.com", "bbc.com", "reuters.com", "nytimes.com", "guardian.com",
            "washingtonpost.com", "bloomberg.com", "techcrunch.com", "verge.com"
        ),
        "Shopping" to listOf(
            "amazon.com", "ebay.com", "etsy.com", "shopify.com", "alibaba.com",
            "walmart.com", "target.com", "bestbuy.com", "flipkart.com"
        ),
        "Development" to listOf(
            "github.com", "stackoverflow.com", "gitlab.com", "bitbucket.org",
            "codepen.io", "jsfiddle.net", "repl.it", "codesandbox.io"
        ),
        "Documentation" to listOf(
            "docs.google.com", "notion.so", "confluence.atlassian.com",
            "gitbook.com", "readthedocs.io", "wiki", "documentation"
        ),
        "Cloud Storage" to listOf(
            "drive.google.com", "dropbox.com", "onedrive.live.com", "icloud.com",
            "box.com", "mega.nz", "pcloud.com"
        ),
        "Education" to listOf(
            "coursera.org", "udemy.com", "edx.org", "khanacademy.org",
            "pluralsight.com", "lynda.com", "skillshare.com", "udacity.com"
        ),
        "Finance" to listOf(
            "paypal.com", "stripe.com", "coinbase.com", "binance.com",
            "robinhood.com", "mint.com", "chase.com", "bankofamerica.com"
        ),
        "Travel" to listOf(
            "booking.com", "airbnb.com", "expedia.com", "tripadvisor.com",
            "kayak.com", "hotels.com", "uber.com", "lyft.com"
        ),
        "Gaming" to listOf(
            "steam.com", "epic.com", "origin.com", "battle.net", "gog.com",
            "itch.io", "roblox.com", "minecraft.net", "twitch.tv"
        ),
        "Productivity" to listOf(
            "trello.com", "asana.com", "slack.com", "zoom.us", "teams.microsoft.com",
            "calendly.com", "todoist.com", "evernote.com", "notion.so"
        ),
        "Design" to listOf(
            "figma.com", "sketch.com", "adobe.com", "canva.com", "dribbble.com",
            "behance.net", "unsplash.com", "pexels.com", "flaticon.com"
        )
    )
    
    // App package mappings for different domains
    private val appMappings = mapOf(
        // Social Media
        "facebook.com" to listOf("com.facebook.katana", "com.facebook.lite"),
        "instagram.com" to listOf("com.instagram.android"),
        "twitter.com" to listOf("com.twitter.android"),
        "x.com" to listOf("com.twitter.android"),
        "linkedin.com" to listOf("com.linkedin.android"),
        "reddit.com" to listOf("com.reddit.frontpage"),
        "discord.com" to listOf("com.discord"),
        "telegram.org" to listOf("org.telegram.messenger"),
        "whatsapp.com" to listOf("com.whatsapp"),
        "tiktok.com" to listOf("com.zhiliaoapp.musically"),
        "snapchat.com" to listOf("com.snapchat.android"),
        "pinterest.com" to listOf("com.pinterest"),
        
        // Video & Entertainment
        "youtube.com" to listOf("com.google.android.youtube", "com.google.android.youtube.tv"),
        "netflix.com" to listOf("com.netflix.mediaclient"),
        "twitch.tv" to listOf("tv.twitch.android.app"),
        "vimeo.com" to listOf("com.vimeo.android.videoapp"),
        "hulu.com" to listOf("com.hulu.plus"),
        "disney" to listOf("com.disney.disneyplus"),
        
        // Music
        "spotify.com" to listOf("com.spotify.music"),
        "soundcloud.com" to listOf("com.soundcloud.android"),
        "pandora.com" to listOf("com.pandora.android"),
        "apple.com/music" to listOf("com.apple.android.music"),
        
        // Shopping
        "amazon.com" to listOf("com.amazon.mShop.android.shopping", "com.amazon.avod.thirdpartyclient"),
        "ebay.com" to listOf("com.ebay.mobile"),
        "etsy.com" to listOf("com.etsy.android"),
        "flipkart.com" to listOf("com.flipkart.android"),
        "walmart.com" to listOf("com.walmart.android"),
        
        // Development
        "github.com" to listOf("com.github.android"),
        "stackoverflow.com" to listOf("com.stackexchange.stackoverflow"),
        
        // Maps & Navigation
        "maps.google.com" to listOf("com.google.android.apps.maps"),
        "waze.com" to listOf("com.waze"),
        
        // Communication
        "zoom.us" to listOf("us.zoom.videomeetings"),
        "teams.microsoft.com" to listOf("com.microsoft.teams"),
        "slack.com" to listOf("com.Slack"),
        "skype.com" to listOf("com.skype.raider"),
        
        // Finance
        "paypal.com" to listOf("com.paypal.android.p2pmobile"),
        "coinbase.com" to listOf("com.coinbase.android"),
        
        // Travel
        "uber.com" to listOf("com.ubercab"),
        "lyft.com" to listOf("me.lyft.android"),
        "airbnb.com" to listOf("com.airbnb.android"),
        "booking.com" to listOf("com.booking"),
        
        // News
        "cnn.com" to listOf("com.cnn.mobile.android.phone"),
        "bbc.com" to listOf("bbc.mobile.news.ww"),
        "nytimes.com" to listOf("com.nytimes.android"),
        
        // Gaming
        "steam.com" to listOf("com.valvesoftware.android.steam.community"),
        "twitch.tv" to listOf("tv.twitch.android.app"),
        
        // Productivity
        "trello.com" to listOf("com.trello"),
        "asana.com" to listOf("com.asana.app"),
        "todoist.com" to listOf("com.todoist"),
        "evernote.com" to listOf("com.evernote"),
        "notion.so" to listOf("notion.id"),
        
        // Cloud Storage
        "drive.google.com" to listOf("com.google.android.apps.docs"),
        "dropbox.com" to listOf("com.dropbox.android"),
        "onedrive.live.com" to listOf("com.microsoft.skydrive")
    )
    
    // Content type patterns
    private val contentPatterns = mapOf(
        "Article" to listOf("/article/", "/blog/", "/post/", "/news/", "/story/"),
        "Video" to listOf("/watch", "/video/", "/v/", "/embed/", "youtube.com/watch"),
        "Image" to listOf(".jpg", ".png", ".gif", ".webp", "/photo/", "/image/"),
        "Document" to listOf(".pdf", ".doc", ".docx", "/document/", "docs.google.com"),
        "Repository" to listOf("github.com/", "gitlab.com/", "bitbucket.org/"),
        "Profile" to listOf("/profile/", "/user/", "/u/", "/@"),
        "Product" to listOf("/product/", "/item/", "/p/", "/dp/"),
        "Course" to listOf("/course/", "/learn/", "/tutorial/", "/class/")
    )
    
    fun categorizeLink(url: String, title: String = "", description: String = ""): LinkCategory {
        val uri = Uri.parse(url)
        val domain = uri.host?.lowercase() ?: ""
        val path = uri.path?.lowercase() ?: ""
        val fullText = "$title $description $url".lowercase()
        
        var bestCategory = "General"
        var bestConfidence = 0.0f
        val suggestedTags = mutableListOf<String>()
        
        // Check domain patterns
        for ((category, domains) in categoryPatterns) {
            for (domainPattern in domains) {
                if (domain.contains(domainPattern)) {
                    val confidence = calculateDomainConfidence(domain, domainPattern)
                    if (confidence > bestConfidence) {
                        bestCategory = category
                        bestConfidence = confidence
                    }
                }
            }
        }
        
        // Check content patterns
        for ((contentType, patterns) in contentPatterns) {
            for (pattern in patterns) {
                if (url.lowercase().contains(pattern) || path.contains(pattern)) {
                    suggestedTags.add(contentType.lowercase())
                    if (bestConfidence < 0.7f) {
                        bestCategory = contentType
                        bestConfidence = 0.6f
                    }
                }
            }
        }
        
        // Analyze title and description for keywords
        val keywordAnalysis = analyzeKeywords(fullText)
        if (keywordAnalysis.confidence > bestConfidence) {
            bestCategory = keywordAnalysis.category
            bestConfidence = keywordAnalysis.confidence
        }
        suggestedTags.addAll(keywordAnalysis.tags)
        
        // Add domain-based tags
        suggestedTags.add(extractDomainTag(domain))
        
        return LinkCategory(
            name = bestCategory,
            icon = getCategoryIcon(bestCategory),
            color = getCategoryColor(bestCategory),
            confidence = bestConfidence,
            suggestedTags = suggestedTags.distinct().filter { it.isNotEmpty() }
        )
    }
    
    fun suggestApps(url: String, context: Context): List<AppSuggestion> {
        val uri = Uri.parse(url)
        val domain = uri.host?.lowercase() ?: ""
        val suggestions = mutableListOf<AppSuggestion>()
        
        // Direct domain mapping
        for ((domainPattern, packages) in appMappings) {
            if (domain.contains(domainPattern)) {
                packages.forEach { packageName ->
                    if (isAppInstalled(context, packageName)) {
                        val appName = getAppName(context, packageName)
                        suggestions.add(
                            AppSuggestion(
                                packageName = packageName,
                                appName = appName,
                                confidence = 0.9f,
                                reason = "Direct domain match"
                            )
                        )
                    }
                }
            }
        }
        
        // Fuzzy domain matching
        if (suggestions.isEmpty()) {
            for ((domainPattern, packages) in appMappings) {
                if (domain.contains(domainPattern.split(".")[0])) {
                    packages.forEach { packageName ->
                        if (isAppInstalled(context, packageName)) {
                            val appName = getAppName(context, packageName)
                            suggestions.add(
                                AppSuggestion(
                                    packageName = packageName,
                                    appName = appName,
                                    confidence = 0.6f,
                                    reason = "Partial domain match"
                                )
                            )
                        }
                    }
                }
            }
        }
        
        // Add browser options
        val browsers = getBrowserApps(context)
        browsers.forEach { (packageName, appName) ->
            suggestions.add(
                AppSuggestion(
                    packageName = packageName,
                    appName = appName,
                    confidence = 0.3f,
                    reason = "Browser option"
                )
            )
        }
        
        return suggestions.sortedByDescending { it.confidence }.take(5)
    }
    
    private fun calculateDomainConfidence(domain: String, pattern: String): Float {
        return when {
            domain == pattern -> 1.0f
            domain.endsWith(".$pattern") -> 0.95f
            domain.contains(pattern) -> 0.8f
            else -> 0.0f
        }
    }
    
    private fun analyzeKeywords(text: String): KeywordAnalysis {
        val keywords = mapOf(
            "Development" to listOf("code", "programming", "developer", "api", "github", "tutorial", "documentation"),
            "Social Media" to listOf("social", "post", "share", "follow", "like", "comment", "profile"),
            "Shopping" to listOf("buy", "shop", "price", "cart", "order", "product", "store"),
            "News" to listOf("news", "article", "breaking", "report", "journalist", "media"),
            "Education" to listOf("learn", "course", "tutorial", "education", "study", "lesson"),
            "Entertainment" to listOf("watch", "movie", "show", "entertainment", "fun", "game"),
            "Business" to listOf("business", "company", "corporate", "professional", "work"),
            "Technology" to listOf("tech", "technology", "software", "hardware", "digital", "innovation")
        )
        
        var bestCategory = "General"
        var bestScore = 0
        val foundTags = mutableListOf<String>()
        
        for ((category, categoryKeywords) in keywords) {
            var score = 0
            for (keyword in categoryKeywords) {
                if (text.contains(keyword)) {
                    score++
                    foundTags.add(keyword)
                }
            }
            if (score > bestScore) {
                bestScore = score
                bestCategory = category
            }
        }
        
        val confidence = minOf(bestScore * 0.1f, 0.8f)
        return KeywordAnalysis(bestCategory, confidence, foundTags)
    }
    
    private fun extractDomainTag(domain: String): String {
        return domain.split(".").firstOrNull { it.length > 2 } ?: ""
    }
    
    private fun getCategoryIcon(category: String): String {
        return when (category) {
            "Social Media" -> "👥"
            "Video Streaming" -> "📺"
            "Music" -> "🎵"
            "News & Media" -> "📰"
            "Shopping" -> "🛒"
            "Development" -> "💻"
            "Documentation" -> "📚"
            "Cloud Storage" -> "☁️"
            "Education" -> "🎓"
            "Finance" -> "💰"
            "Travel" -> "✈️"
            "Gaming" -> "🎮"
            "Productivity" -> "⚡"
            "Design" -> "🎨"
            "Article" -> "📄"
            "Video" -> "🎬"
            "Image" -> "🖼️"
            "Document" -> "📋"
            "Repository" -> "📦"
            "Profile" -> "👤"
            "Product" -> "🏷️"
            "Course" -> "📖"
            else -> "🔗"
        }
    }
    
    private fun getCategoryColor(category: String): String {
        return when (category) {
            "Social Media" -> "#1DA1F2"
            "Video Streaming" -> "#FF0000"
            "Music" -> "#1DB954"
            "News & Media" -> "#FF6B35"
            "Shopping" -> "#FF9500"
            "Development" -> "#6F42C1"
            "Documentation" -> "#0366D6"
            "Cloud Storage" -> "#4285F4"
            "Education" -> "#28A745"
            "Finance" -> "#FFC107"
            "Travel" -> "#17A2B8"
            "Gaming" -> "#6F42C1"
            "Productivity" -> "#20C997"
            "Design" -> "#E83E8C"
            else -> "#6C757D"
        }
    }
    
    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    private fun getAppName(context: Context, packageName: String): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            val appInfo = packageInfo.applicationInfo
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }
    
    private fun getBrowserApps(context: Context): List<Pair<String, String>> {
        val browsers = mutableListOf<Pair<String, String>>()
        val browserPackages = listOf(
            "com.android.chrome",
            "com.chrome.beta",
            "com.chrome.dev",
            "com.chrome.canary",
            "org.mozilla.firefox",
            "org.mozilla.firefox_beta",
            "com.microsoft.emmx",
            "com.opera.browser",
            "com.opera.browser.beta",
            "com.brave.browser",
            "com.duckduckgo.mobile.android",
            "com.samsung.android.app.sbrowser",
            "com.UCMobile.intl",
            "mark.via.gp"
        )
        
        browserPackages.forEach { packageName ->
            if (isAppInstalled(context, packageName)) {
                val appName = getAppName(context, packageName)
                browsers.add(packageName to appName)
            }
        }
        
        return browsers
    }
    
    data class KeywordAnalysis(
        val category: String,
        val confidence: Float,
        val tags: List<String>
    )
}
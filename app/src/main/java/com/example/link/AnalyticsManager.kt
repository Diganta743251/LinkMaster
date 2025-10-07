package com.example.link

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsManager(private val linkDao: LinkDao) {
    
    data class LinkStatistics(
        val totalLinks: Int,
        val favoriteLinks: Int,
        val totalClicks: Int,
        val averageClicksPerLink: Double,
        val mostClickedLink: Link?,
        val recentlyAddedLinks: List<Link>,
        val topTags: List<TagStatistic>,
        val clicksThisWeek: Int,
        val clicksThisMonth: Int,
        val linksAddedThisWeek: Int,
        val linksAddedThisMonth: Int,
        val oldestLink: Link?,
        val newestLink: Link?
    )
    
    data class TagStatistic(
        val tag: String,
        val count: Int,
        val totalClicks: Int
    )
    
    data class DailyStatistic(
        val date: String,
        val linksAdded: Int,
        val totalClicks: Int
    )
    
    fun getStatistics(): Flow<LinkStatistics> {
        return combine(
            linkDao.getAllLinks(),
            linkDao.getFavoriteLinks()
        ) { allLinks, favoriteLinks ->
            
            val totalClicks = allLinks.sumOf { it.clicks }
            val averageClicks = if (allLinks.isNotEmpty()) totalClicks.toDouble() / allLinks.size else 0.0
            val mostClickedLink = allLinks.maxByOrNull { it.clicks }
            
            // Recent links (last 7 days)
            val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
            val recentLinks = allLinks.filter { it.createdAt >= weekAgo }
                .sortedByDescending { it.createdAt }
                .take(10)
            
            // Tag statistics
            val tagStats = calculateTagStatistics(allLinks)
            
            // Time-based statistics
            val monthAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)
            val clicksThisWeek = allLinks.filter { it.lastAccessedAt >= weekAgo }.sumOf { it.clicks }
            val clicksThisMonth = allLinks.filter { it.lastAccessedAt >= monthAgo }.sumOf { it.clicks }
            val linksThisWeek = allLinks.count { it.createdAt >= weekAgo }
            val linksThisMonth = allLinks.count { it.createdAt >= monthAgo }
            
            val oldestLink = allLinks.minByOrNull { it.createdAt }
            val newestLink = allLinks.maxByOrNull { it.createdAt }
            
            LinkStatistics(
                totalLinks = allLinks.size,
                favoriteLinks = favoriteLinks.size,
                totalClicks = totalClicks,
                averageClicksPerLink = averageClicks,
                mostClickedLink = mostClickedLink,
                recentlyAddedLinks = recentLinks,
                topTags = tagStats,
                clicksThisWeek = clicksThisWeek,
                clicksThisMonth = clicksThisMonth,
                linksAddedThisWeek = linksThisWeek,
                linksAddedThisMonth = linksThisMonth,
                oldestLink = oldestLink,
                newestLink = newestLink
            )
        }
    }
    
    fun getDailyStatistics(days: Int = 30): Flow<List<DailyStatistic>> {
        return linkDao.getAllLinks().combine(linkDao.getAllLinks()) { allLinks, _ ->
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val statistics = mutableListOf<DailyStatistic>()
            
            repeat(days) { dayOffset ->
                calendar.timeInMillis = System.currentTimeMillis() - (dayOffset * 24 * 60 * 60 * 1000)
                val dayStart = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                
                val dayEnd = dayStart + (24 * 60 * 60 * 1000) - 1
                
                val linksAddedThisDay = allLinks.count { it.createdAt in dayStart..dayEnd }
                val clicksThisDay = allLinks.filter { it.lastAccessedAt in dayStart..dayEnd }.sumOf { it.clicks }
                
                statistics.add(
                    DailyStatistic(
                        date = dateFormat.format(Date(dayStart)),
                        linksAdded = linksAddedThisDay,
                        totalClicks = clicksThisDay
                    )
                )
            }
            
            statistics.reversed() // Most recent first
        }
    }
    
    private fun calculateTagStatistics(links: List<Link>): List<TagStatistic> {
        val tagMap = mutableMapOf<String, MutableList<Link>>()
        
        links.forEach { link ->
            if (link.tags.isNotEmpty()) {
                link.tags.split(",").forEach { tag ->
                    val cleanTag = tag.trim()
                    if (cleanTag.isNotEmpty()) {
                        tagMap.getOrPut(cleanTag) { mutableListOf() }.add(link)
                    }
                }
            }
        }
        
        return tagMap.map { (tag, tagLinks) ->
            TagStatistic(
                tag = tag,
                count = tagLinks.size,
                totalClicks = tagLinks.sumOf { it.clicks }
            )
        }.sortedByDescending { it.count }.take(10)
    }
    
    fun getTopDomains(limit: Int = 10): Flow<List<DomainStatistic>> {
        return linkDao.getAllLinks().combine(linkDao.getAllLinks()) { allLinks, _ ->
            val domainMap = mutableMapOf<String, MutableList<Link>>()
            
            allLinks.forEach { link ->
                try {
                    val domain = extractDomain(link.url)
                    if (domain.isNotEmpty()) {
                        domainMap.getOrPut(domain) { mutableListOf() }.add(link)
                    }
                } catch (e: Exception) {
                    // Skip invalid URLs
                }
            }
            
            domainMap.map { (domain, domainLinks) ->
                DomainStatistic(
                    domain = domain,
                    linkCount = domainLinks.size,
                    totalClicks = domainLinks.sumOf { it.clicks },
                    averageClicks = if (domainLinks.isNotEmpty()) domainLinks.sumOf { it.clicks }.toDouble() / domainLinks.size else 0.0
                )
            }.sortedByDescending { it.linkCount }.take(limit)
        }
    }
    
    private fun extractDomain(url: String): String {
        return try {
            val cleanUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else url
            
            val uri = java.net.URI(cleanUrl)
            uri.host?.lowercase() ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}

data class DomainStatistic(
    val domain: String,
    val linkCount: Int,
    val totalClicks: Int,
    val averageClicks: Double
)
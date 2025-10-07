package com.example.link.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

data class Language(
    val code: String,
    val name: String,
    val nativeName: String
)

object LanguageManager {
    private const val PREF_LANGUAGE = "app_language"
    private const val DEFAULT_LANGUAGE = "en"
    
    private val availableLanguages = listOf(
        Language("en", "English", "English"),
        Language("es", "Spanish", "Español"),
        Language("fr", "French", "Français"),
        Language("de", "German", "Deutsch"),
        Language("it", "Italian", "Italiano"),
        Language("pt", "Portuguese", "Português"),
        Language("ru", "Russian", "Русский"),
        Language("zh", "Chinese", "中文"),
        Language("ja", "Japanese", "日本語"),
        Language("ko", "Korean", "한국어"),
        Language("ar", "Arabic", "العربية"),
        Language("hi", "Hindi", "हिन्दी"),
        Language("bn", "Bengali", "বাংলা"),
        Language("ur", "Urdu", "اردو"),
        Language("ta", "Tamil", "தமிழ்"),
        Language("te", "Telugu", "తెలుగు"),
        Language("ml", "Malayalam", "മലയാളം"),
        Language("kn", "Kannada", "ಕನ್ನಡ"),
        Language("gu", "Gujarati", "ગુજરાતી"),
        Language("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        Language("mr", "Marathi", "मराठी"),
        Language("or", "Odia", "ଓଡ଼ିଆ"),
        Language("as", "Assamese", "অসমীয়া"),
        Language("ne", "Nepali", "नेपाली"),
        Language("si", "Sinhala", "සිංහල"),
        Language("my", "Myanmar", "မြန်မာ"),
        Language("th", "Thai", "ไทย"),
        Language("vi", "Vietnamese", "Tiếng Việt"),
        Language("id", "Indonesian", "Bahasa Indonesia"),
        Language("ms", "Malay", "Bahasa Melayu"),
        Language("tl", "Filipino", "Filipino"),
        Language("sw", "Swahili", "Kiswahili"),
        Language("am", "Amharic", "አማርኛ"),
        Language("tr", "Turkish", "Türkçe"),
        Language("fa", "Persian", "فارسی"),
        Language("he", "Hebrew", "עברית"),
        Language("pl", "Polish", "Polski"),
        Language("cs", "Czech", "Čeština"),
        Language("sk", "Slovak", "Slovenčina"),
        Language("hu", "Hungarian", "Magyar"),
        Language("ro", "Romanian", "Română"),
        Language("bg", "Bulgarian", "Български"),
        Language("hr", "Croatian", "Hrvatski"),
        Language("sr", "Serbian", "Српски"),
        Language("sl", "Slovenian", "Slovenščina"),
        Language("et", "Estonian", "Eesti"),
        Language("lv", "Latvian", "Latviešu"),
        Language("lt", "Lithuanian", "Lietuvių"),
        Language("fi", "Finnish", "Suomi"),
        Language("sv", "Swedish", "Svenska"),
        Language("da", "Danish", "Dansk"),
        Language("no", "Norwegian", "Norsk"),
        Language("is", "Icelandic", "Íslenska"),
        Language("nl", "Dutch", "Nederlands"),
        Language("ca", "Catalan", "Català"),
        Language("eu", "Basque", "Euskera"),
        Language("gl", "Galician", "Galego"),
        Language("cy", "Welsh", "Cymraeg"),
        Language("ga", "Irish", "Gaeilge"),
        Language("mt", "Maltese", "Malti"),
        Language("sq", "Albanian", "Shqip"),
        Language("mk", "Macedonian", "Македонски"),
        Language("be", "Belarusian", "Беларуская"),
        Language("uk", "Ukrainian", "Українська"),
        Language("ka", "Georgian", "ქართული"),
        Language("hy", "Armenian", "Հայերեն"),
        Language("az", "Azerbaijani", "Azərbaycan"),
        Language("kk", "Kazakh", "Қазақша"),
        Language("ky", "Kyrgyz", "Кыргызча"),
        Language("uz", "Uzbek", "O'zbek"),
        Language("tg", "Tajik", "Тоҷикӣ"),
        Language("mn", "Mongolian", "Монгол"),
        Language("bo", "Tibetan", "བོད་ཡིག"),
        Language("km", "Khmer", "ខ្មែរ"),
        Language("lo", "Lao", "ລາວ"),
        Language("ka", "Georgian", "ქართული"),
        Language("am", "Amharic", "አማርኛ")
    )
    
    fun getAvailableLanguages(): List<Language> = availableLanguages
    
    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return prefs.getString(PREF_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }
    
    fun setLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, languageCode).apply()
        
        updateConfiguration(context, languageCode)
    }
    
    fun updateConfiguration(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
    
    fun getCurrentLanguageName(context: Context): String {
        val currentCode = getLanguage(context)
        return availableLanguages.find { it.code == currentCode }?.name ?: "English"
    }
    
    fun getCurrentLanguageNativeName(context: Context): String {
        val currentCode = getLanguage(context)
        return availableLanguages.find { it.code == currentCode }?.nativeName ?: "English"
    }
    
    fun isRTL(languageCode: String): Boolean {
        return when (languageCode) {
            "ar", "fa", "he", "ur" -> true
            else -> false
        }
    }
    
    fun getLanguageByCode(code: String): Language? {
        return availableLanguages.find { it.code == code }
    }
    
    fun getSupportedLanguageCodes(): List<String> {
        return availableLanguages.map { it.code }
    }
    
    fun getSystemLanguage(): String {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault()
        } else {
            @Suppress("DEPRECATION")
            Locale.getDefault()
        }
        
        val systemLanguageCode = systemLocale.language
        return if (getSupportedLanguageCodes().contains(systemLanguageCode)) {
            systemLanguageCode
        } else {
            DEFAULT_LANGUAGE
        }
    }
    
    fun initializeLanguage(context: Context) {
        val savedLanguage = getLanguage(context)
        if (savedLanguage == DEFAULT_LANGUAGE) {
            // First time - try to use system language if supported
            val systemLanguage = getSystemLanguage()
            setLanguage(context, systemLanguage)
        } else {
            updateConfiguration(context, savedLanguage)
        }
    }
}
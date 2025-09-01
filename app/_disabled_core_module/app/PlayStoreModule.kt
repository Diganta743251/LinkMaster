package fe.linksheet.module.app

import fe.linksheet.module.performance.PerformanceOptimizer
import fe.linksheet.module.privacy.LocalInsights
import fe.linksheet.module.ux.UserExperienceEnhancer
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Play Store compliant module configuration
 * Provides enhanced features without violating policies
 */
val PlayStoreModule = module {
    // Performance optimization
    single { PerformanceOptimizer(androidApplication()) }
    
    // User experience enhancements
    single { UserExperienceEnhancer(androidApplication()) }
    
    // Privacy-focused local insights
    single { LocalInsights(androidApplication()) }
}

/**
 * Enhanced UI/UX module for better user experience
 */
val EnhancedUIModule = module {
    // UI performance settings
    factory { get<PerformanceOptimizer>().getUIPerformanceSettings() }
    
    // Bottom sheet optimizations
    factory { get<PerformanceOptimizer>().optimizeBottomSheetPerformance() }
    
    // Device capabilities
    factory { get<UserExperienceEnhancer>().getDeviceCapabilities() }
    
    // Animation specifications
    factory { get<UserExperienceEnhancer>().getEnhancedAnimations() }
    
    // Haptic patterns
    factory { get<UserExperienceEnhancer>().getHapticPatterns() }
    
    // Accessibility enhancements
    factory { get<UserExperienceEnhancer>().getAccessibilityEnhancements() }
}
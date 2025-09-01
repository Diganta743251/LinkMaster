package fe.linksheet

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import app.linksheet.testing.Testing
import com.google.android.material.color.DynamicColors
import fe.linksheet.ads.AdManager
import fe.linksheet.ads.ConsentManager
import fe.android.lifecycle.CurrentActivityObserver
import fe.android.lifecycle.ProcessServiceRegistry
import fe.android.lifecycle.koin.extension.applicationLifecycle
import fe.composekit.core.AndroidVersion
import fe.droidkit.koin.androidApplicationContext
import fe.linksheet.module.gson.GlobalGsonModule
import fe.linksheet.module.gson.GlobalGsonContext
import fe.linksheet.activity.CrashHandlerActivity
import fe.linksheet.log.AndroidLogSink
import fe.linksheet.log.LLog
// Analytics modules removed - no backend connectivity needed
import fe.linksheet.module.app.PackageModule
import fe.linksheet.module.clock.ClockModule
import fe.linksheet.module.database.dao.module.DaoModule
import fe.linksheet.module.database.DatabaseModule
// Debug modules removed - not needed for production
import fe.linksheet.module.devicecompat.CompatModule
import fe.linksheet.module.devicecompat.miui.MiuiCompatProvider
import fe.linksheet.module.devicecompat.miui.RealMiuiCompatProvider
import fe.linksheet.module.devicecompat.oneui.OneUiCompatProvider
import fe.linksheet.module.devicecompat.oneui.RealOneUiCompatProvider
// Downloader module removed - external network access
import fe.linksheet.module.language.AppLocaleModule
// HTTP and network modules removed - no backend connectivity needed
import fe.linksheet.module.log.DefaultLogModule
import fe.linksheet.module.log.file.entry.LogEntry
import fe.linksheet.module.log.file.entry.LogEntryDeserializer
// Paste service module removed - no backend connectivity needed
import fe.linksheet.module.preference.PreferenceRepositoryModule
import fe.linksheet.module.preference.state.AppStateServiceModule
import fe.linksheet.module.profile.ProfileSwitcherModule
// Remote config modules removed - no backend connectivity needed
import fe.linksheet.module.repository.module.RepositoryModule
import fe.linksheet.module.resolver.module.ResolverModule
// UrlResolverModule removed - no backend connectivity needed
// import fe.linksheet.module.resolver.urlresolver.UrlResolverModule
// Shizuku and statistics modules removed
import fe.linksheet.module.systeminfo.SystemInfoServiceModule
// Version tracker and work manager modules removed
import fe.linksheet.module.viewmodel.module.ViewModelModule
import fe.linksheet.util.serialization.HttpUrlTypeAdapter
import fe.linksheet.util.serialization.UriTypeAdapter
// NetworkStateServiceModule removed - no backend connectivity needed
// import fe.composekit.lifecycle.network.koin.NetworkStateServiceModule
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.ext.koin.androidLogger

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
// import org.lsposed.hiddenapibypass.HiddenApiBypass // Removed for Play Store compliance
import java.time.LocalDateTime
import kotlin.system.exitProcess


open class LinkSheetApp : Application(), DependencyProvider {
    val startupTime: LocalDateTime = LocalDateTime.now()

    private val currentActivityObserver = CurrentActivityObserver()
    private val lifecycleObserver by lazy { ProcessServiceRegistry() }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(currentActivityObserver)
        LLog.addSink(AndroidLogSink())

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val crashIntent = Intent(this, CrashHandlerActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(CrashHandlerActivity.EXTRA_CRASH_EXCEPTION, Log.getStackTraceString(throwable))
                .putExtra(CrashHandlerActivity.EXTRA_CRASH_TIMESTAMP, System.currentTimeMillis())

            startActivity(crashIntent)
            exitProcess(2)
        }

        GlobalGsonContext.configure {
            registerTypeAdapter(LogEntry::class.java, LogEntryDeserializer)
            UriTypeAdapter.register(this)
            HttpUrlTypeAdapter.register(this)
        }

        // Hidden API bypass removed for Play Store compliance
        // if (AndroidVersion.isAtLeastApi28P() && !Testing.IsTestRunner) {
        //     HiddenApiBypass.addHiddenApiExemptions("")
        // }

        DynamicColors.applyToActivitiesIfAvailable(this)
        
        // Analytics and ads removed - violates Play Store policies

        val koinModules = provideKoinModules()
        val koinApplication = startKoin {
            androidLogger()
            androidApplicationContext(this@LinkSheetApp)
            applicationLifecycle(lifecycleObserver)

            modules(koinModules)
        }

        lifecycleObserver.onAppInitialized()
    }

    // AdMob initialization removed - violates Play Store policies

    override fun provideKoinModules(): List<Module> {
        return listOf(
            ClockModule,
            SystemInfoServiceModule,
            PackageModule,
            AppLocaleModule,
            // Network service module removed - no backend connectivity needed
            // Shizuku module removed - system API access violates Play Store policies
            GlobalGsonModule,
            PreferenceRepositoryModule,
            DefaultLogModule,
            provideCompatProvider(),
            CompatModule,
            DatabaseModule,
            DaoModule,
            RepositoryModule,
            // HTTP and remote config modules removed - no backend connectivity needed
            // UrlResolverModule removed - no backend connectivity needed
            ResolverModule,
            ViewModelModule,
            // Downloader module removed - external network access
            // Analytics modules removed - no backend connectivity needed
            // Statistics module removed - privacy concerns
            // Version tracker module removed - uses analytics
            // Paste service module removed - no backend connectivity needed
            ProfileSwitcherModule,
            AppStateServiceModule,
            // Enhanced modules for better user experience
            fe.linksheet.module.app.PlayStoreModule,
            fe.linksheet.module.app.EnhancedUIModule,
            // Debug module removed - not needed for production
            // Work delegator service removed - background processing
        )
    }

    override fun provideCompatProvider(): Module {
        return module {
            single<MiuiCompatProvider> { RealMiuiCompatProvider(get()) }
            single<OneUiCompatProvider> { RealOneUiCompatProvider(get()) }
        }
    }

    // Analytics client removed - no backend connectivity needed

    // Debug module removed - not needed for production

    fun currentActivity(): StateFlow<Activity?> {
        return currentActivityObserver.current
    }
}

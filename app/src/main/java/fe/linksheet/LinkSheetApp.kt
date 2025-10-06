package fe.linksheet

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.android.material.color.DynamicColors
import fe.linksheet.activity.CrashHandlerActivity
import fe.linksheet.log.AndroidLogSink
import fe.linksheet.log.LLog
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
// import org.lsposed.hiddenapibypass.HiddenApiBypass // Removed for Play Store compliance
import java.time.LocalDateTime
import kotlin.system.exitProcess


open class LinkSheetApp : Application(), DependencyProvider {
    val startupTime: LocalDateTime = LocalDateTime.now()

    override fun onCreate() {
        super.onCreate()
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

        // Hidden API bypass removed for Play Store compliance
        // if (AndroidVersion.isAtLeastApi28P() && !Testing.IsTestRunner) {
        //     HiddenApiBypass.addHiddenApiExemptions("")
        // }

        DynamicColors.applyToActivitiesIfAvailable(this)

        val koinModules = provideKoinModules()
        startKoin {
            androidLogger()
            androidContext(this@LinkSheetApp)

            modules(koinModules)
        }
    }

    override fun provideKoinModules(): List<Module> {
        return listOf(
            provideCompatProvider(),
            provideAnalyticsClient(),
            provideDebugModule(),
            // Minimal bindings required by services
            module { single { fe.linksheet.module.repository.PreferredAppRepository() } },
        )
    }

    override fun provideCompatProvider(): Module {
        // Device compatibility providers disabled; provide empty module
        return module { }
    }

    override fun provideAnalyticsClient(): Module = module { }
    override fun provideDebugModule(): Module = module { }
}

package fe.linksheet.activity

import android.os.Bundle

class CrashHandlerActivity : BaseComponentActivity() {
    companion object {
        const val EXTRA_CRASH_EXCEPTION = "EXTRA_CRASH_EXCEPTION_TEXT"
        const val EXTRA_CRASH_TIMESTAMP = "EXTRA_CRASH_TIMESTAMP"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Minimal crash handler: simply finish. Full UI removed to reduce dependencies for assembleDebug.
        finish()
    }
}

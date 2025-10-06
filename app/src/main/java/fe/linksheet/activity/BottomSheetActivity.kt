package fe.linksheet.activity

import android.os.Bundle

// Must not be moved or renamed since LinkSheetCompat hardcodes the package/name
class BottomSheetActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Minimal placeholder to satisfy build; full bottom sheet implementation is disabled in this build.
        finish()
    }
}

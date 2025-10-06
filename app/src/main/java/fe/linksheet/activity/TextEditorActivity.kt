package fe.linksheet.activity

import android.os.Bundle
class TextEditorActivity : BaseComponentActivity() {
    companion object {
        const val EXTRA_TEXT = "EXTRA_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Minimal placeholder activity; full editor removed for lightweight build.
        setResult(RESULT_CANCELED)
        finish()
    }
}

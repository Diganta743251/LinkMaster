package fe.linksheet.activity.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import fe.linksheet.activity.UiEventReceiverBaseComponentActivity
import androidx.compose.material3.Text


class MainActivity : UiEventReceiverBaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(edgeToEdge = true) {
            Text("LinkSheet")
        }
    }
}

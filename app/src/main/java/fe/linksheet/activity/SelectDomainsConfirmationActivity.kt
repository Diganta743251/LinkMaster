package fe.linksheet.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.IntentCompat
import androidx.core.os.bundleOf
import fe.linksheet.BuildConfig
import fe.linksheet.R
import fe.linksheet.interconnect.IDomainSelectionResultCallback
import fe.linksheet.interconnect.StringParceledListSlice

class SelectDomainsConfirmationActivity : BaseComponentActivity() {
    companion object {
        const val ACTION_CONFIRM = "${BuildConfig.APPLICATION_ID}.action.CONFIRM_SELECTION"
        const val EXTRA_CALLING_COMPONENT = "calling_component"
        const val EXTRA_CALLING_PACKAGE = "calling_package"
        const val EXTRA_DOMAINS = "domains"
        const val EXTRA_CALLBACK = "callback"

        fun start(
            context: Context,
            callingPackage: String,
            callingComponent: ComponentName,
            domains: StringParceledListSlice,
            callback: IDomainSelectionResultCallback? = null,
        ) {
            val intent = Intent(context, SelectDomainsConfirmationActivity::class.java)

            intent.action = ACTION_CONFIRM
            intent.putExtra(EXTRA_CALLING_PACKAGE, callingPackage)
            intent.putExtra(EXTRA_DOMAINS, domains)
            intent.putExtra(EXTRA_CALLING_COMPONENT, callingComponent)
            intent.putExtra(EXTRA_CALLBACK, bundleOf(EXTRA_CALLBACK to callback?.asBinder()))
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action != ACTION_CONFIRM) {
            finish()
            return
        }

        val callingComponent = IntentCompat.getParcelableExtra(
            intent,
            EXTRA_CALLING_COMPONENT,
            ComponentName::class.java,
        )
        val callingPackage = intent.getStringExtra(EXTRA_CALLING_PACKAGE)
        val domains = IntentCompat.getParcelableExtra(
            intent,
            EXTRA_DOMAINS,
            StringParceledListSlice::class.java
        )?.list
        val callback = intent.getBundleExtra(EXTRA_CALLBACK)?.getBinder(EXTRA_CALLBACK)?.let {
            IDomainSelectionResultCallback.Stub.asInterface(it)
        }

        if (callingPackage == null || domains == null || callingComponent == null) {
            finish()
            return
        }
        // Minimal stub: do not show UI or persist. Simply notify callback (if provided) and finish.
        try {
            callback?.onDomainSelectionConfirmed()
        } catch (_: Throwable) {
            // ignore callback errors in minimal build
        }
        finish()
    }
}

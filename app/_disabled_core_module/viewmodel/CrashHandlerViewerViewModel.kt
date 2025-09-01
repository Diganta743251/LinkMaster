package fe.linksheet.module.viewmodel

import android.app.Application
import android.content.ClipboardManager
import androidx.core.content.getSystemService
import fe.linksheet.module.log.file.LogPersistService
import fe.linksheet.module.preference.app.AppPreferenceRepository
// Experiment repository import removed - violates Play Store policies
import fe.linksheet.module.viewmodel.base.BaseViewModel
import fe.linksheet.module.viewmodel.util.LogViewCommon

class CrashHandlerViewerViewModel(
    context: Application,
    val preferenceRepository: AppPreferenceRepository,
    // experimentsRepository: ExperimentRepository, // Removed - violates Play Store policies
    val logViewCommon: LogViewCommon,
    val logPersistService: LogPersistService
) : BaseViewModel(preferenceRepository) {
    val clipboardManager = context.getSystemService<ClipboardManager>()!!
}

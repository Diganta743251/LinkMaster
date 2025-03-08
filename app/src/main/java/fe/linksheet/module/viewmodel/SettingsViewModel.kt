package fe.linksheet.module.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import fe.linksheet.module.preference.app.AppPreferenceRepository


import fe.linksheet.module.preference.app.AppPreferences
import fe.linksheet.module.viewmodel.base.BaseViewModel

class SettingsViewModel(
    val context: Application,
    preferenceRepository: AppPreferenceRepository
) : BaseViewModel(preferenceRepository) {
    val devModeEnabled = preferenceRepository.asViewModelState(AppPreferences.devModeEnabled)
}

package fe.linksheet.module.resolver.util

import android.content.Intent

sealed class LaunchIntent(val intent: Intent)
class LaunchRawIntent(intent: Intent) : LaunchIntent(intent)
class LaunchMainIntent(intent: Intent) : LaunchIntent(intent)
class LaunchViewIntent(intent: Intent) : LaunchIntent(intent)

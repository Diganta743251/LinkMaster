package fe.linksheet.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import fe.linksheet.R
import fe.linksheet.activity.main.MainActivity

/**
 * LinkMaster Home Widget - Play Store friendly implementation
 * Provides quick access to frequently used links
 */
class LinkMasterWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update each widget instance
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Create RemoteViews for the widget layout
        val views = RemoteViews(context.packageName, R.layout.widget_linkmaster)

        // Set up click intent to open main app
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set click listeners for widget elements
        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)
        views.setOnClickPendingIntent(R.id.widget_link_1, pendingIntent)
        views.setOnClickPendingIntent(R.id.widget_link_2, pendingIntent)
        views.setOnClickPendingIntent(R.id.widget_link_3, pendingIntent)

        // Set up refresh button
        val refreshIntent = Intent(context, LinkMasterWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context,
            appWidgetId,
            refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh, refreshPendingIntent)

        // Update widget text content
        views.setTextViewText(R.id.widget_title, "LinkMaster")
        views.setTextViewText(R.id.widget_link_text_1, "Quick Links")
        views.setTextViewText(R.id.widget_link_text_2, "Recent")
        views.setTextViewText(R.id.widget_link_text_3, "Favorites")

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onEnabled(context: Context) {
        // Called when the first widget is created
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        // Called when the last widget is removed
        super.onDisabled(context)
    }
}
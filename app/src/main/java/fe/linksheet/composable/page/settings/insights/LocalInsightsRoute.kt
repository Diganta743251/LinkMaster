package fe.linksheet.composable.page.settings.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fe.linksheet.R
import fe.linksheet.composable.component.page.SaneScaffoldSettingsPage
import fe.linksheet.module.privacy.LocalInsights

@Composable
fun LocalInsightsRoute(
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val localInsights = remember { LocalInsights(context) }
    val insights by localInsights.insights.collectAsStateWithLifecycle()
    
    SaneScaffoldSettingsPage(
        headline = stringResource(R.string.local_insights),
        onBackPressed = onBackPressed
    ) {
        item {
            LocalInsightsContent(
                insights = insights,
                onClearData = { localInsights.clearAllData() }
            )
        }
    }
}

@Composable
private fun LocalInsightsContent(
    insights: fe.linksheet.module.privacy.LocalInsightsData,
    onClearData: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Privacy Notice Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.privacy_first),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = stringResource(R.string.local_insights_privacy_notice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Usage Summary Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = null)
                    Text(
                        text = stringResource(R.string.usage_summary),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = insights.usageSummary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (insights.totalInteractions > 0) {
                    Text(
                        text = stringResource(R.string.total_interactions, insights.totalInteractions),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Statistics Card
        if (insights.totalInteractions > 0) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.local_statistics),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InsightItem(
                            label = stringResource(R.string.app_launches),
                            value = insights.appLaunches.toString(),
                            icon = Icons.Default.Launch
                        )
                        InsightItem(
                            label = stringResource(R.string.links_processed),
                            value = insights.linksProcessed.toString(),
                            icon = Icons.Default.Link
                        )
                        InsightItem(
                            label = stringResource(R.string.qr_codes_generated),
                            value = insights.qrCodesGenerated.toString(),
                            icon = Icons.Default.QrCode
                        )
                        InsightItem(
                            label = stringResource(R.string.themes_changed),
                            value = insights.themeChanges.toString(),
                            icon = Icons.Default.Palette
                        )
                        InsightItem(
                            label = stringResource(R.string.widget_interactions),
                            value = insights.widgetInteractions.toString(),
                            icon = Icons.Default.Widgets
                        )
                    }
                }
            }
        }

        // Usage Timeline Card
        if (insights.firstUsed != null || insights.lastUsed != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.usage_timeline),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    insights.firstUsed?.let { firstUsed ->
                        InsightItem(
                            label = stringResource(R.string.first_used),
                            value = firstUsed.take(10), // Show date only
                            icon = Icons.Default.Start
                        )
                    }
                    
                    insights.lastUsed?.let { lastUsed ->
                        InsightItem(
                            label = stringResource(R.string.last_used),
                            value = lastUsed.take(10), // Show date only
                            icon = Icons.Default.Schedule
                        )
                    }
                }
            }
        }

        // Clear Data Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.clear_local_data),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = stringResource(R.string.clear_local_data_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                OutlinedButton(
                    onClick = onClearData,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.clear_data))
                }
            }
        }
    }
}

@Composable
private fun InsightItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
package fe.linksheet.composable.page.settings.privacy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fe.linksheet.R
import fe.linksheet.data.LinkHistoryDuration
import fe.linksheet.data.LinkHistoryLimit

@Composable
fun LinkHistoryDurationDialog(
    currentDuration: LinkHistoryDuration,
    onDurationSelected: (LinkHistoryDuration) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDuration by remember { mutableStateOf(currentDuration) }
    var customDays by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(currentDuration is LinkHistoryDuration.CUSTOM_DAYS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.link_history_duration_dialog_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Disabled option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedDuration == LinkHistoryDuration.DISABLED,
                            onClick = {
                                selectedDuration = LinkHistoryDuration.DISABLED
                                showCustomInput = false
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedDuration == LinkHistoryDuration.DISABLED,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Disabled (never delete)")
                }

                // Predefined options
                val predefinedOptions = listOf(
                    LinkHistoryDuration.ONE_DAY to "1 day",
                    LinkHistoryDuration.SEVEN_DAYS to "7 days",
                    LinkHistoryDuration.FOURTEEN_DAYS to "14 days",
                    LinkHistoryDuration.THIRTY_DAYS to "30 days"
                )

                predefinedOptions.forEach { (duration, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedDuration == duration,
                                onClick = {
                                    selectedDuration = duration
                                    showCustomInput = false
                                },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDuration == duration,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label)
                    }
                }

                // Custom option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedDuration is LinkHistoryDuration.CUSTOM_DAYS,
                            onClick = {
                                showCustomInput = true
                                val currentDuration = selectedDuration
                                if (currentDuration is LinkHistoryDuration.CUSTOM_DAYS) {
                                    customDays = currentDuration.days.toString()
                                } else {
                                    customDays = ""
                                    selectedDuration = LinkHistoryDuration.CUSTOM_DAYS(0)
                                }
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedDuration is LinkHistoryDuration.CUSTOM_DAYS,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.custom_days))
                }

                // Custom input field
                if (showCustomInput) {
                    OutlinedTextField(
                        value = customDays,
                        onValueChange = { value ->
                            customDays = value
                            val days = value.toIntOrNull() ?: 0
                            selectedDuration = LinkHistoryDuration.CUSTOM_DAYS(days)
                        },
                        label = { Text(stringResource(R.string.enter_days)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDurationSelected(selectedDuration)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.back))
            }
        }
    )
}

@Composable
fun LinkHistoryLimitDialog(
    currentLimit: LinkHistoryLimit,
    onLimitSelected: (LinkHistoryLimit) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLimit by remember { mutableStateOf(currentLimit) }
    var customCount by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(currentLimit is LinkHistoryLimit.CUSTOM_COUNT) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.link_history_limit_dialog_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // No limit option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedLimit is LinkHistoryLimit.NO_LIMIT,
                            onClick = {
                                selectedLimit = LinkHistoryLimit.NO_LIMIT
                                showCustomInput = false
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLimit is LinkHistoryLimit.NO_LIMIT,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("No limit")
                }

                // Predefined options
                val predefinedOptions = listOf(
                    LinkHistoryLimit.ONE_HUNDRED to "Last 100 links",
                    LinkHistoryLimit.ONE_THOUSAND to "Last 1000 links"
                )

                predefinedOptions.forEach { (limit, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedLimit == limit,
                                onClick = {
                                    selectedLimit = limit
                                    showCustomInput = false
                                },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLimit == limit,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label)
                    }
                }

                // Custom option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedLimit is LinkHistoryLimit.CUSTOM_COUNT,
                            onClick = {
                                showCustomInput = true
                                val currentLimit = selectedLimit
                                if (currentLimit is LinkHistoryLimit.CUSTOM_COUNT) {
                                    customCount = currentLimit.count.toString()
                                } else {
                                    customCount = ""
                                    selectedLimit = LinkHistoryLimit.CUSTOM_COUNT(0)
                                }
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLimit is LinkHistoryLimit.CUSTOM_COUNT,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.custom_count))
                }

                // Custom input field
                if (showCustomInput) {
                    OutlinedTextField(
                        value = customCount,
                        onValueChange = { value ->
                            customCount = value
                            val count = value.toIntOrNull() ?: 0
                            selectedLimit = LinkHistoryLimit.CUSTOM_COUNT(count)
                        },
                        label = { Text(stringResource(R.string.enter_count)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLimitSelected(selectedLimit)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.back))
            }
        }
    )
}

@Composable
fun ClearHistoryDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.clear_history_dialog_title))
        },
        text = {
            Text(text = stringResource(R.string.clear_history_dialog_message))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.clear))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.back))
            }
        }
    )
}
package fe.linksheet.module.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fe.linksheet.feature.sql.MarkdownTable
import fe.linksheet.feature.sql.SqlRow
import fe.linksheet.feature.sql.SqlQueryFeature
import fe.linksheet.feature.sql.Column
import fe.linksheet.module.database.LinkSheetDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class SqlViewModel(val database: LinkSheetDatabase) : ViewModel() {
    // TODO: Implement proper database access when LinkSheetDatabase is fully implemented
    private val table = MarkdownTable()

    val rows = SnapshotStateList<SqlRow>()
    val text = mutableStateOf<String?>(null)

    fun run(query: String) = viewModelScope.launch(Dispatchers.IO) {
        rows.clear()
        // TODO: Implement actual SQL query execution
        // For now, just add a placeholder row
        rows.add(SqlRow("message" to Column.StringValue("SQL feature not yet implemented")))
        text.value = table.create(rows)
    }
}


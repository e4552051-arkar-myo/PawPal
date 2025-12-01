package uk.ac.tees.mad.e4552051.pawpal.ui.components

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    val context = LocalContext.current

    TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onTimeSelected(hour, minute)
        },
        12,
        0,
        true
    ).apply {
        setOnDismissListener { onDismissRequest() }
        show()
    }
}
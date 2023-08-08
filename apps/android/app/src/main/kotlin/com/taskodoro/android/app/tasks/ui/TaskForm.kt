/*
 *    Copyright 2023 Green Peaks Studio
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.taskodoro.android.app.tasks.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taskodoro.android.R
import com.taskodoro.android.app.ui.components.TaskodoroTemplate
import com.taskodoro.android.app.ui.components.TaskodoroTextField
import com.taskodoro.android.app.ui.theme.TaskodoroTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

/**
 * Reusable Task creation/edition form composable.
 */
@Composable
fun TaskForm(
    title: String,
    description: String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDueDateChanged: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TitleTextField(
            title = title,
            onTitleChanged = onTitleChanged,
        )

        DescriptionTextField(
            description = description,
            onDescriptionChanged = onDescriptionChanged,
        )

        Text(
            text = "Due on",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.paddingFromBaseline(top = 32.dp, bottom = 8.dp),
        )

        DueDatePicker(onDueDateChanged)

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TitleTextField(
    title: String,
    onTitleChanged: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val titleLabel = stringResource(id = R.string.task_form_title)

    TaskodoroTextField(
        value = title,
        onValueChanged = onTitleChanged,
        placeHolderText = titleLabel,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        modifier = Modifier.focusRequester(focusRequester),
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun DescriptionTextField(
    description: String,
    onDescriptionChanged: (String) -> Unit,
) {
    val descriptionLabel = stringResource(id = R.string.task_form_description)
    TaskodoroTextField(
        value = description,
        onValueChanged = onDescriptionChanged,
        placeHolderText = descriptionLabel,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        modifier = Modifier
            .defaultMinSize(minHeight = 128.dp),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DueDatePicker(
    onDueDateChanged: (Long) -> Unit,
) {
    val today = LocalDate.now()
        .atStartOfDay()
        .asMillis(ZoneOffset.UTC)

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = today,
    )

    DatePicker(
        state = datePickerState,
        dateValidator = { it >= today },
        title = null,
        headline = null,
        showModeToggle = false,
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { onDueDateChanged(it.asSeconds()) }
    }
}

private fun Long.asSeconds() = TimeUnit.MILLISECONDS.toSeconds(this)

private fun LocalDateTime.asMillis(offset: ZoneOffset) =
    TimeUnit.SECONDS.toMillis(toEpochSecond(offset))

@Preview(
    name = "Day Mode",
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Night Mode",
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TaskFormPreview() {
    TaskodoroTheme {
        TaskForm(
            title = "",
            onTitleChanged = {},
            description = "",
            onDescriptionChanged = {},
            onDueDateChanged = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        )
    }
}

@Preview(
    name = "Day Mode",
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Night Mode",
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TaskFormWithErrorsPreview() {
    TaskodoroTemplate {
        TaskForm(
            title = "",
            onTitleChanged = {},
            description = "",
            onDescriptionChanged = {},
            onDueDateChanged = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        )
    }
}

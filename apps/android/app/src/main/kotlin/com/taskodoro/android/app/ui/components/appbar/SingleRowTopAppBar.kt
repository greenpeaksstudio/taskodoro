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

package com.taskodoro.android.app.ui.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taskodoro.android.app.ui.components.appbar.model.ActionsList
import com.taskodoro.android.app.ui.components.appbar.model.TopAppBarAction
import com.taskodoro.android.app.ui.components.preview.ComponentPreviews
import com.taskodoro.android.app.ui.components.preview.FontScalePreviews
import com.taskodoro.android.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleRowTopAppBar(
    title: String,
    navigationIcon: TopAppBarAction.Icon,
    modifier: Modifier = Modifier,
    actions: ActionsList = ActionsList(listOf()),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@FontScalePreviews
@ComponentPreviews
@Composable
private fun SingleRowTopAppBarPreview() {
    AppTheme {
        SingleRowTopAppBar(
            title = "TopAppBar",
            navigationIcon = TopAppBarAction.Icon(
                icon = Icons.Rounded.ArrowBack,
                contentDescription = "Back",
                action = { },
            ),
            actions = ActionsList(
                listOf(
                    TopAppBarAction.Button(
                        text = "Save",
                        action = { },
                    ),
                ),
            ),
        )
    }
}

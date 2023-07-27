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

package com.taskodoro.tasks

import com.taskodoro.tasks.model.Task
import com.taskodoro.tasks.model.TaskValidationResult

class CreateTaskUseCase(
    private val repository: TaskRepository,
    private val validate: (Task) -> TaskValidationResult,
) {

     operator fun invoke(task: Task): Result<Unit> =
        try {
            when (validate(task)) {
                TaskValidationResult.EMPTY_TITLE ->
                    Result.failure(TaskRepository.TaskException.EmptyTitle)

                TaskValidationResult.INVALID_TITLE ->
                    Result.failure(TaskRepository.TaskException.InvalidTitle)

                TaskValidationResult.SUCCESS -> repository.save(task.withTrimmedValues())
            }
        } catch (exception: TaskRepository.TaskException) {
            Result.failure(exception)
        }

    private fun Task.withTrimmedValues(): Task = copy(title = title.trim())
}
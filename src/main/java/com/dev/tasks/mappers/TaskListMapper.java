package com.dev.tasks.mappers;

import com.dev.tasks.domain.dto.TaskListDto;
import com.dev.tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList toTaskList(TaskListDto taskListDto);

    TaskListDto toTaskListDto(TaskList taskList);
}

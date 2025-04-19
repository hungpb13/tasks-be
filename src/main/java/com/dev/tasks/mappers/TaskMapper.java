package com.dev.tasks.mappers;

import com.dev.tasks.domain.dto.TaskDto;
import com.dev.tasks.domain.entities.Task;

public interface TaskMapper {

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);
}

package com.dev.tasks.mappers.impl;

import com.dev.tasks.domain.dto.TaskListDto;
import com.dev.tasks.domain.entities.Task;
import com.dev.tasks.domain.entities.TaskList;
import com.dev.tasks.domain.entities.TaskStatus;
import com.dev.tasks.mappers.TaskListMapper;
import com.dev.tasks.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {

    private final TaskMapper taskMapper;

    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList toTaskList(TaskListDto taskListDto) {
        return TaskList.builder()
                .id(taskListDto.id())
                .title(taskListDto.title())
                .description(taskListDto.description())
                .tasks(Optional.ofNullable(taskListDto.tasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::toEntity)
                                .toList()
                        ).orElse(null)
                )
                .build();
    }

    @Override
    public TaskListDto toTaskListDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks())
                        .map(List::size)
                        .orElse(0),
                calculateTaskListProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::toDto)
                                .toList()
                        )
                        .orElse(null)

        );
    }

    private Double calculateTaskListProgress(List<Task> tasks) {
        if (tasks == null || tasks.size() == 0) {
            return null;
        }

        long closedTaskCount = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.CLOSED)
                .count();

        return (double) (closedTaskCount / tasks.size());
    }
}

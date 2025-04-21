package com.dev.tasks.services.iml;

import com.dev.tasks.domain.entities.TaskList;
import com.dev.tasks.repositories.TaskListRepository;
import com.dev.tasks.services.TaskListService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {
        if (taskList.getId() != null) {
            throw new IllegalArgumentException("Task list already has  an ID!");
        }

        if (taskList.getTitle() == null || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present!");
        }

        LocalDateTime now = LocalDateTime.now();

        return taskListRepository.save(TaskList.builder()
                .title(taskList.getTitle())
                .description(taskList.getDescription())
                .created(now)
                .updated(now)
                .build());
    }
}

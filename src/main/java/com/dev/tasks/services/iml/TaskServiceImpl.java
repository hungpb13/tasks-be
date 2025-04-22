package com.dev.tasks.services.iml;

import com.dev.tasks.domain.entities.Task;
import com.dev.tasks.domain.entities.TaskList;
import com.dev.tasks.domain.entities.TaskPriority;
import com.dev.tasks.domain.entities.TaskStatus;
import com.dev.tasks.repositories.TaskListRepository;
import com.dev.tasks.repositories.TaskRepository;
import com.dev.tasks.services.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Transactional
    @Override
    public Task createTask(UUID taskListId, Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("Task already has an ID!");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task must have a title!");
        }

        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);

        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task list ID!"));

        LocalDateTime now = LocalDateTime.now();

        Task taskToSave = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(taskPriority)
                .status(taskStatus)
                .taskList(taskList)
                .created(now)
                .updated(now)
                .build();

        return taskRepository.save(taskToSave);
    }

    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId) {
        return taskRepository.findByTaskListIdAndId(taskListId, taskId);
    }

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task must have a ID!");
        }

        if (!Objects.equals(task.getId(), taskId)) {
            throw new IllegalArgumentException("Task IDs do not match!");
        }

        if (task.getPriority() == null) {
            throw new IllegalArgumentException("Task must have a valid priority!");
        }

        if (task.getStatus() == null) {
            throw new IllegalArgumentException("Task must have a valid status!");
        }

        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        task.setPriority(task.getPriority());
        task.setStatus(task.getStatus());
        task.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }
}

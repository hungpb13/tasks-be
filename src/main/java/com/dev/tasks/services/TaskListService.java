package com.dev.tasks.services;

import com.dev.tasks.domain.entities.TaskList;

import java.util.List;

public interface TaskListService {

    List<TaskList> listTaskLists();

    TaskList createTaskList(TaskList taskList);
}

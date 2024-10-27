package com.example.taskmanagement.service;

import com.example.task.management.model.TaskDto;
import com.example.task.management.model.TasksResponse;
import com.example.taskmanagement.repository.entity.Priority;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);

    TasksResponse getAllTasks(String status, String sort, Integer page, Integer size);

    void deleteTask(Long id);

    void completeTask(Long id);

    TaskDto updateTask(Long id, TaskDto taskDto);

    void notifyTasks(Priority priority);
}

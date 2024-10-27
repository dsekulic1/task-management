package com.example.taskmanagement.controller;

import com.example.task.management.api.TaskManagementApi;
import com.example.task.management.model.TaskDto;
import com.example.task.management.model.TasksResponse;
import com.example.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskManagementController implements TaskManagementApi {
    private final TaskService taskService;

    @Override
    public ResponseEntity<Void> completeTask(Long id) {
        taskService.completeTask(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TaskDto> createTask(TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TasksResponse> getAllTasks(String status, String sort, Integer page, Integer size) {
        return ResponseEntity.ok(taskService.getAllTasks(status, sort, page, size));
    }

    @Override
    public ResponseEntity<TaskDto> updateTask(Long id, TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }
}

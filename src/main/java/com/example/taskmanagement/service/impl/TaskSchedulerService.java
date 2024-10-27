package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.repository.entity.Priority;
import com.example.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class TaskSchedulerService {

    private final TaskService taskService;

    @Scheduled(fixedDelayString = "${task.notification.low}")
    public void notifyLowPriorityTasks() {
        taskService.notifyTasks(Priority.LOW);
    }

    @Scheduled(fixedDelayString = "${task.notification.medium}")
    public void notifyMediumPriorityTasks() {
        taskService.notifyTasks(Priority.MEDIUM);
    }

    @Scheduled(fixedDelayString = "${task.notification.high}")
    public void notifyHighPriorityTasks() {
        taskService.notifyTasks(Priority.HIGH);
    }
}

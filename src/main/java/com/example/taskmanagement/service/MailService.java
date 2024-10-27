package com.example.taskmanagement.service;

import com.example.task.management.model.TaskDto;

public interface MailService {
    void sendEmail(TaskDto taskDto);

    void sendReminderEmail(TaskDto task);
}

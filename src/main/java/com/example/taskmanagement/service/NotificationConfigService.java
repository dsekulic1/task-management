package com.example.taskmanagement.service;

import com.example.notification.config.model.NotificationConfigRequest;


public interface NotificationConfigService {
    void configureNotifiedStatuses(NotificationConfigRequest notificationConfigRequest);
}

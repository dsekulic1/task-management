package com.example.taskmanagement.service.impl;

import com.example.notification.config.model.NotificationConfigRequest;
import com.example.taskmanagement.config.NotificationConfig;
import com.example.taskmanagement.repository.entity.Status;
import com.example.taskmanagement.service.NotificationConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationConfigServiceImpl implements NotificationConfigService {
    private final NotificationConfig notificationConfig;

    @Override
    public void configureNotifiedStatuses(NotificationConfigRequest notificationConfigRequest) {
        List<String> statuses = notificationConfigRequest.getStatuses();

        if (statuses == null || statuses.isEmpty())
            notificationConfig.setDefault();
        else {
            List<Status> validatedStatuses = new ArrayList<>();

            statuses.forEach(s -> {
                try {
                    Status status = Status.valueOf(s.toUpperCase());
                    validatedStatuses.add(status);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid status value: " + s);
                }
            });

            notificationConfig.setNotifiedStatuses(validatedStatuses);
        }
    }
}

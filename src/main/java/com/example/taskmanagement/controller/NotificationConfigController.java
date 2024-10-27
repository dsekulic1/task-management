package com.example.taskmanagement.controller;

import com.example.notification.config.api.NotificationConfigApi;

import com.example.notification.config.model.NotificationConfigRequest;
import com.example.taskmanagement.service.NotificationConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationConfigController implements NotificationConfigApi {
    private final NotificationConfigService notificationConfigService;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> configureStatusNotifications(NotificationConfigRequest notificationConfigRequest) {
        notificationConfigService.configureNotifiedStatuses(notificationConfigRequest);
        return ResponseEntity.noContent().build();
    }
}

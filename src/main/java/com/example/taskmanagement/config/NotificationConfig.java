package com.example.taskmanagement.config;

import com.example.taskmanagement.repository.entity.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class NotificationConfig {

    private List<Status> notifiedStatuses = new ArrayList<>();

    public NotificationConfig() {
        setDefault();
    }

    public void setDefault() {
        notifiedStatuses.clear();
        notifiedStatuses.add(Status.PENDING);
        notifiedStatuses.add(Status.IN_PROGRESS);
        notifiedStatuses.add(Status.OVERDUE);
    }

    public boolean shouldNotify(Status status) {
        return notifiedStatuses.contains(status);
    }
}

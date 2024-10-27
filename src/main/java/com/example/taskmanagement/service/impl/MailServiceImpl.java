package com.example.taskmanagement.service.impl;

import com.example.task.management.model.TaskDto;
import com.example.taskmanagement.service.MailService;
import com.example.taskmanagement.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String mailFrom;

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(TaskDto task) {
        try {
            SimpleMailMessage mailMessage = getCommonMailMassageData(task);

            mailMessage.setText(createEmailBody(task));

            emailSender.send(mailMessage);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    public void sendReminderEmail(TaskDto task) {
        try {
            SimpleMailMessage mailMessage = getCommonMailMassageData(task);

            mailMessage.setText(createReminderEmailBody(task));

            emailSender.send(mailMessage);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private SimpleMailMessage getCommonMailMassageData(TaskDto task) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(task.getAssignedUserEmail());
        mailMessage.setSubject(task.getTitle());

        return mailMessage;
    }

    private String createEmailBody(TaskDto task) {
        return String.format(
                """
                Dear,
        
                You have been assigned a new task with the following details:
        
                Description: %s
                Due Date: %s
                Priority: %s
                Status: %s
                Category: %s
        
                Please make sure to complete the task on time.
        
                Best regards,
                Task Management System
                """,
                task.getDescription(),
                DateUtils.formatZonedDate(task.getDueDate()),
                task.getPriority(),
                task.getStatus(),
                task.getCategory()
        );
    }

    private String createReminderEmailBody(TaskDto task) {
        return """
            Dear,

            This is a reminder for the following task:

            Description: %s
            Due Date: %s
            Priority: %s
            Status: %s
            Category: %s

            Please complete the task before the due date.

            Best regards,
            Task Management System
            """.formatted(
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                task.getCategory()
        );
    }
}

package com.example.taskmanagement.service;

import com.example.task.management.model.TaskDto;
import com.example.taskmanagement.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {
    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto();
        taskDto.setTitle("Email Test Task");
        taskDto.setDescription("This is email test task.");
        taskDto.setAssignedUserEmail("test@gmail.com");
        taskDto.setPriority("MEDIUM");
        taskDto.setStatus("PENDING");
        taskDto.setCategory("WORK");
        taskDto.setDueDate("2024-10-30T23:59:59Z");
    }

    @Test
    void testSendEmail() {
        mailService.sendEmail(taskDto);

        var mailMessageCaptor = forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(mailMessageCaptor.capture());

        SimpleMailMessage mailMessage = mailMessageCaptor.getValue();

        assertThat(mailMessage.getTo()).containsExactly(taskDto.getAssignedUserEmail());
        assertThat(mailMessage.getSubject()).isEqualTo(taskDto.getTitle());
        assertThat(mailMessage.getText()).contains("You have been assigned a new task with the following details:");
        assertThat(mailMessage.getText()).contains(taskDto.getDescription());
    }

    @Test
    void testSendReminderEmail() {
        mailService.sendReminderEmail(taskDto);

        var mailMessageCaptor = forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(mailMessageCaptor.capture());

        SimpleMailMessage mailMessage = mailMessageCaptor.getValue();

        assertThat(mailMessage.getTo()).containsExactly(taskDto.getAssignedUserEmail());
        assertThat(mailMessage.getSubject()).isEqualTo(taskDto.getTitle());
        assertThat(mailMessage.getText()).contains("This is a reminder for the following task:");
        assertThat(mailMessage.getText()).contains(taskDto.getDescription());
    }
}

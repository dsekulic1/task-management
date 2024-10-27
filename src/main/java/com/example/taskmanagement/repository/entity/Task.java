package com.example.taskmanagement.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Data
@RequiredArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private Category category;

    @Column(name = "ASSIGNED_USER_EMAIL", nullable = false)
    @Email
    private String assignedUserEmail;
}

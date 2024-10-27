package com.example.taskmanagement.repository;

import com.example.taskmanagement.repository.entity.Priority;
import com.example.taskmanagement.repository.entity.Status;
import com.example.taskmanagement.repository.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(Status status, Pageable pageable);

    List<Task> findByPriority(Priority priority);
}

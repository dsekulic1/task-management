package com.example.taskmanagement.service;

import com.example.task.management.model.TaskDto;
import com.example.task.management.model.TasksResponse;
import com.example.taskmanagement.controller.exception.NotFoundException;
import com.example.taskmanagement.controller.exception.TaskProcessingException;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.entity.Category;
import com.example.taskmanagement.repository.entity.Priority;
import com.example.taskmanagement.repository.entity.Status;
import com.example.taskmanagement.repository.entity.Task;
import com.example.taskmanagement.service.impl.TaskServiceImpl;
import com.example.taskmanagement.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto();
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Description of the test task");
        taskDto.setPriority("MEDIUM");
        taskDto.setStatus("PENDING");
        taskDto.setDueDate("2024-10-30T23:59:59Z");
        taskDto.setCategory("WORK");
        taskDto.setAssignedUserEmail("test@example.com");

        task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(Priority.valueOf(taskDto.getPriority().toUpperCase()));
        task.setStatus(Status.valueOf(taskDto.getStatus().toUpperCase()));
        task.setCategory(Category.valueOf(taskDto.getCategory().toUpperCase()));
        task.setAssignedUserEmail(taskDto.getAssignedUserEmail());
    }

    @Test
    void testCreateTask() {
        when(modelMapper.map(taskDto, Task.class)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto createdTask = taskService.createTask(taskDto);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("Test Task");
        verify(mailService, times(1)).sendEmail(taskDto);
    }

    @Test
    void testCreateTaskThrowsException() {
        when(modelMapper.map(taskDto, Task.class)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> taskService.createTask(taskDto))
                .isInstanceOf(TaskProcessingException.class)
                .hasMessageContaining("Failed to create task");
    }

    @Test
    void testGetAllTasks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(task), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(modelMapper.map(any(Task.class), eq(TaskDto.class))).thenReturn(taskDto);

        TasksResponse response = taskService.getAllTasks(null, null, 0, 10);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(Constants.TASK_NOT_FOUND);
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        taskDto.setTitle("Updated task");

        TaskDto updatedTask = taskService.updateTask(1L, taskDto);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo("Updated task");
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(2L, taskDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(Constants.TASK_NOT_FOUND);
    }
}

package com.example.taskmanagement.service.impl;

import com.example.task.management.model.TaskDto;
import com.example.task.management.model.TasksResponse;
import com.example.taskmanagement.config.NotificationConfig;
import com.example.taskmanagement.controller.exception.NotFoundException;
import com.example.taskmanagement.controller.exception.TaskProcessingException;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.entity.Category;
import com.example.taskmanagement.repository.entity.Priority;
import com.example.taskmanagement.repository.entity.Status;
import com.example.taskmanagement.repository.entity.Task;
import com.example.taskmanagement.service.MailService;
import com.example.taskmanagement.service.TaskService;
import com.example.taskmanagement.utils.Constants;
import com.example.taskmanagement.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final NotificationConfig notificationConfig;

    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        validateEnums(taskDto);
        try {
            Task task = modelMapper.map(taskDto, Task.class);

            Task savedTask = taskRepository.save(task);
            mailService.sendEmail(taskDto);

            return modelMapper.map(savedTask, TaskDto.class);
        } catch (Exception exception) {
            log.error("Error occurred while creating task: {}", exception.getMessage());
            throw new TaskProcessingException("Failed to create task: " + exception.getMessage(), exception);
        }
    }

    @Override
    public TasksResponse getAllTasks(String status, String sort, Integer page, Integer size) {
        Pageable pageable;

        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(":");
            String field = sortParams[0];
            String order = sortParams.length > 1 ? sortParams[1] : "asc";

            Sort sortOrder = "desc".equalsIgnoreCase(order) ? Sort.by(field).descending() : Sort.by(field).ascending();
            pageable = PageRequest.of(page, size, sortOrder);
        } else {
            pageable = PageRequest.of(page, size);
        }

        Page<Task> taskPage;

        if (status != null && !status.isEmpty()) {
            taskPage = taskRepository.findByStatus(Status.valueOf(status.toUpperCase()), pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        List<TaskDto> taskDtoList = taskPage.getContent().stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .toList();

        TasksResponse response = new TasksResponse();
        response.setContent(taskDtoList);
        response.setTotalPages(taskPage.getTotalPages());
        response.setTotalElements((int) taskPage.getTotalElements());
        response.setSize(taskPage.getSize());
        response.setPage(taskPage.getNumber());

        return response;
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException(Constants.TASK_NOT_FOUND);
        }

        taskRepository.deleteById(id);
    }

    @Override
    public void completeTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException(Constants.TASK_NOT_FOUND));

        task.setStatus(Status.COMPLETED);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException(Constants.TASK_NOT_FOUND));

        validateEnums(taskDto);

        try {
            existingTask.setTitle(taskDto.getTitle());
            existingTask.setDescription(taskDto.getDescription());
            existingTask.setDueDate(DateUtils.getDateFromStringDate(taskDto.getDueDate()));
            existingTask.setPriority(Priority.valueOf(taskDto.getPriority().toUpperCase()));
            existingTask.setStatus(Status.valueOf(taskDto.getStatus().toUpperCase()));
            existingTask.setCategory(Category.valueOf(taskDto.getCategory().toUpperCase()));
            existingTask.setAssignedUserEmail(taskDto.getAssignedUserEmail());

            Task updatedTask = taskRepository.save(existingTask);

            return modelMapper.map(updatedTask, TaskDto.class);
        } catch (Exception exception) {
            log.error("Error occurred while updating task: {}", exception.getMessage());
            throw new TaskProcessingException("Failed to update task: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void notifyTasks(Priority priority) {
        List<Task> tasks = taskRepository.findByPriority(priority);

        List<Task> filteredTasks = tasks.stream()
                .filter(task -> notificationConfig.shouldNotify(task.getStatus()))
                .toList();

        if (!filteredTasks.isEmpty()) {
            log.info("Sending notifications for priority: " + priority.name());

            for (Task task : filteredTasks) {
                TaskDto taskDto = modelMapper.map(task, TaskDto.class);

                mailService.sendReminderEmail(taskDto);
            }
        } else {
            log.info("No notification sent for priority: " + priority.name());
        }
    }

    private void validateEnums(TaskDto taskDto) {
        try {
            Priority.valueOf(taskDto.getPriority().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority value: " + taskDto.getPriority());
        }

        try {
            Status.valueOf(taskDto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + taskDto.getStatus());
        }

        try {
            Category.valueOf(taskDto.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category value: " + taskDto.getCategory());
        }
    }
}

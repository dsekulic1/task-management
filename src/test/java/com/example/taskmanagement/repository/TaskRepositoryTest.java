package com.example.taskmanagement.repository;

import com.example.taskmanagement.repository.entity.Priority;
import com.example.taskmanagement.repository.entity.Status;
import com.example.taskmanagement.repository.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveAndFindTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setDueDate(new Date());
        task.setPriority(Priority.MEDIUM);
        task.setStatus(Status.PENDING);
        task.setAssignedUserEmail("test@gmail.com");
        Task savedTask = taskRepository.save(task);

        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo("Test Task");
        assertThat(foundTask.getDescription()).isEqualTo("Description");
    }

    @Test
    void testSaveMultipleTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("First task");
        task1.setDueDate(new Date());
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.PENDING);
        task1.setAssignedUserEmail("user1@gmail.com");

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Second task");
        task2.setDueDate(new Date());
        task2.setPriority(Priority.LOW);
        task2.setStatus(Status.COMPLETED);
        task2.setAssignedUserEmail("user2@gmail.com");

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle).containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setTitle("Original Task");
        task.setDescription("Description for original task");
        task.setDueDate(new Date());
        task.setPriority(Priority.LOW);
        task.setStatus(Status.PENDING);
        task.setAssignedUserEmail("orginal@gmail.com");
        Task savedTask = taskRepository.save(task);

        savedTask.setTitle("Updated Task");
        savedTask.setAssignedUserEmail("update@gmail.com");
        taskRepository.save(savedTask);

        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo("Updated Task");
        assertThat(foundTask.getAssignedUserEmail()).isEqualTo("update@gmail.com");
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setTitle("Task to Delete");
        task.setDescription("This task will be deleted");
        task.setDueDate(new Date());
        task.setPriority(Priority.MEDIUM);
        task.setStatus(Status.PENDING);
        task.setAssignedUserEmail("delete@gmail.com");
        Task savedTask = taskRepository.save(task);

        taskRepository.delete(savedTask);
        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        assertThat(foundTask).isNull();
    }

    @Test
    void testFindByStatus() {
        Task task1 = new Task();
        task1.setTitle("Pending Task");
        task1.setDescription("A task that is pending");
        task1.setDueDate(new Date());
        task1.setPriority(Priority.MEDIUM);
        task1.setStatus(Status.PENDING);
        task1.setAssignedUserEmail("pending@gmail.com");

        Task task2 = new Task();
        task2.setTitle("Completed Task");
        task2.setDescription("A task that is completed");
        task2.setDueDate(new Date());
        task2.setPriority(Priority.LOW);
        task2.setStatus(Status.COMPLETED);
        task2.setAssignedUserEmail("completed@gmail.com");

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> pendingTasks = taskRepository.findByStatus(Status.PENDING, PageRequest.of(0, 10)).getContent();

        assertThat(pendingTasks).hasSize(1);
        assertThat(pendingTasks.get(0).getTitle()).isEqualTo("Pending Task");
    }
}

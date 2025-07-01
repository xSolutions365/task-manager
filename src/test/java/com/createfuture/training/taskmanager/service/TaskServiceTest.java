package com.createfuture.training.taskmanager.service;

import com.createfuture.training.taskmanager.model.Task;
import com.createfuture.training.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TaskRepository.class);
    }

    @Test
    void addTask_ShouldAddTaskToRepository() {
        String title = "Task 1";
        taskService.addTask(title);

        String sql = "SELECT COUNT(*) FROM tasks WHERE title = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, title);

        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        taskService.addTask("Task 1");
        taskService.addTask("Task 2");

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }

    @Test
    void getTopNTasks_ShouldReturnTopNTasks() {
        taskService.addTask("Task 1");
        taskService.addTask("Task 2");
        taskService.addTask("Task 3");

        List<Task> topTasks = taskService.getTopNTasks(2);

        assertEquals(2, topTasks.size());
        assertEquals("Task 1", topTasks.get(0).getTitle());
        assertEquals("Task 2", topTasks.get(1).getTitle());
    }

    @Test
    void getTopNTasks_ShouldReturnEmptyListWhenNIsZero() {
        when(repository.findTopN(0)).thenReturn(Collections.emptyList());

        List<Task> top0Tasks = taskService.getTopNTasks(0);
        assertTrue(top0Tasks.isEmpty());
    }

    @Test
    void getTopNTasks_ShouldReturnAllTasksWhenNIsGreaterThanSize() {
        taskService.addTask("Task 1");
        taskService.addTask("Task 2");

        List<Task> tasks = taskService.getTopNTasks(5); // N > total

        assertEquals(2, tasks.size());
    }

    @Test
    void markDone_ShouldRemoveTask() {
        Task task = new Task("Task 1");
        taskService.addTask(task.getTitle());

        Long taskId = taskService.getAllTasks().get(0).getId(); // Retrieve the ID of the added task
        boolean result = taskService.markDone(taskId);

        assertTrue(result);

        List<Task> tasks = taskService.getAllTasks();
        assertTrue(tasks.stream().noneMatch(t -> t.getId().equals(taskId)));
    }

    @Test
    void markDone_ShouldReturnFalseIfTaskNotFound() {
        boolean result = taskService.markDone(999L); // Use a non-existent ID
        assertFalse(result);
    }

    @Test
    void searchTasks_ShouldReturnMatchingTasks() {
        taskService.addTask("feed dog");
        taskService.addTask("walk dog");
        taskService.addTask("buy milk");

        List<Task> results = taskService.searchTasks("dog");
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(t -> t.getTitle().equalsIgnoreCase("feed dog")));
        assertTrue(results.stream().anyMatch(t -> t.getTitle().equalsIgnoreCase("walk dog")));
    }

    @Test
    void searchTasks_ShouldBeCaseInsensitive() {
        taskService.addTask("Feed Dog");
        List<Task> results = taskService.searchTasks("dog");
        assertEquals(1, results.size());
        assertEquals("Feed Dog", results.get(0).getTitle());
    }

    @Test
    void searchTasks_ShouldReturnEmptyListIfNoMatch() {
        taskService.addTask("feed cat");
        List<Task> results = taskService.searchTasks("dog");
        assertTrue(results.isEmpty());
    }
}

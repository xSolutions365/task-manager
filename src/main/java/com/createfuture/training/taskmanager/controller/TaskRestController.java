package com.createfuture.training.taskmanager.controller;

import com.createfuture.training.taskmanager.model.Task;
import com.createfuture.training.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /api/tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // GET /api/tasks/top?n=5
    @GetMapping("/top")
    public ResponseEntity<List<Task>> getTopNTasks(@RequestParam(defaultValue = "5") int n) {
        List<Task> topTasks = taskService.getTopNTasks(n);
        return ResponseEntity.ok(topTasks);
    }

    // POST /api/tasks
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.addTask(task.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // PATCH /api/tasks/{id}/done
    @PatchMapping("/{id}/done")
    public ResponseEntity<Void> markTaskAsDone(@PathVariable Long id) {
        boolean markedDone = taskService.markDone(id);
        return markedDone ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // PUT /api/tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task.getTitle());
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }
}

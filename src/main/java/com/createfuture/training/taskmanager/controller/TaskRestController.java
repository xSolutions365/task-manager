package com.createfuture.training.taskmanager.controller;

import com.createfuture.training.taskmanager.model.Task;
import com.createfuture.training.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:9002")
@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /api/tasks
    @Operation(summary = "Get all tasks")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of all tasks returned")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // GET /api/tasks/search?q=dog
    @Operation(summary = "Search tasks by title")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of matching tasks returned")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam("q") String query) {
        List<Task> results = taskService.searchTasks(query);
        return ResponseEntity.ok(results);
    }

    // GET /api/tasks/top?n=5
    @Operation(summary = "Get top N tasks")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of top N tasks returned")
    })
    @GetMapping("/top")
    public ResponseEntity<List<Task>> getTopNTasks(@RequestParam(defaultValue = "5") int n) {
        List<Task> topTasks = taskService.getTopNTasks(n);
        return ResponseEntity.ok(topTasks);
    }

    // POST /api/tasks
    @Operation(summary = "Create a new task")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Task created successfully")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.addTask(task.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // DELETE /api/tasks/{id}
    @Operation(summary = "Delete a task by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // PATCH /api/tasks/{id}/done
    @Operation(summary = "Mark a task as done")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Task marked as done"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/done")
    public ResponseEntity<Void> markTaskAsDone(@PathVariable Long id) {
        boolean markedDone = taskService.markDone(id);
        return markedDone ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // PUT /api/tasks/{id}
    @Operation(summary = "Update a task by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task.getTitle());
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }
}

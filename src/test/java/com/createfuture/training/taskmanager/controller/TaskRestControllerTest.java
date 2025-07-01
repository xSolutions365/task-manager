package com.createfuture.training.taskmanager.controller;

import com.createfuture.training.taskmanager.model.Task;
import com.createfuture.training.taskmanager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
class TaskRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    @DisplayName("GET /api/tasks should return all tasks")
    void shouldReturnAllTasks() throws Exception {
        List<Task> mockTasks = Arrays.asList(new Task("Task 1"), new Task("Task 2"));
        when(taskService.getAllTasks()).thenReturn(mockTasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }

    @Test
    @DisplayName("GET /api/tasks/top?n=1 should return top N tasks")
    void shouldReturnTopNTasks() throws Exception {
        List<Task> topTasks = List.of(new Task("Top Task"));
        when(taskService.getTopNTasks(1)).thenReturn(topTasks);

        mockMvc.perform(get("/api/tasks/top?n=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].title", is("Top Task")));
    }

    @Test
    @DisplayName("POST /api/tasks should create a task")
    void shouldCreateTask() throws Exception {
        Task newTask = new Task("New Task");
        when(taskService.addTask("New Task")).thenReturn(newTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Task\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Task")));
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} should delete a task")
    void shouldDeleteTask() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} should return 404 if task not found")
    void shouldReturnNotFoundWhenDeletingMissingTask() throws Exception {
        when(taskService.markDone(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} should update a task")
    void shouldUpdateTask() throws Exception {
        Task updatedTask = new Task(1L, "Updated Task");
        when(taskService.updateTask(1L, "Updated Task")).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Task")));
    }

    @Test
    @DisplayName("PATCH /api/tasks/{id}/done should mark a task as done")
    void shouldMarkTaskAsDone() throws Exception {
        when(taskService.markDone(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/tasks/1/done"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/tasks/search?q=dog should return matching tasks")
    void shouldReturnMatchingTasksForSearch() throws Exception {
        List<Task> mockTasks = Arrays.asList(new Task("feed dog"), new Task("walk dog"));
        when(taskService.searchTasks("dog")).thenReturn(mockTasks);

        mockMvc.perform(get("/api/tasks/search?q=dog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", containsStringIgnoringCase("dog")))
                .andExpect(jsonPath("$[1].title", containsStringIgnoringCase("dog")));
    }

    @Test
    @DisplayName("GET /api/tasks/search?q=cat should return empty list if no match")
    void shouldReturnEmptyListIfNoMatchForSearch() throws Exception {
        when(taskService.searchTasks("cat")).thenReturn(List.of());

        mockMvc.perform(get("/api/tasks/search?q=cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }
}

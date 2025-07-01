package com.createfuture.training.taskmanager.service;

import com.createfuture.training.taskmanager.model.Task;
import com.createfuture.training.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(String title) {
        return taskRepository.addTask(title);
    }

    public List<Task> getAllTasks() {
        // Only return tasks that are not done
        return taskRepository.findAll().stream()
                .filter(task -> !task.isDone())
                .toList(); 
    }

    public List<Task> getTopNTasks(int n) {
        return taskRepository.findTopN(n);
    }

    public boolean markDone(Long id) {
        // Mark the task as done, but do not delete it
        return taskRepository.markDoneById(id) > 0;
    }

    public void resetTasks() {
        taskRepository.reset();
    }

    public boolean deleteTask(Long id) {
        return taskRepository.deleteById(id) > 0;
    }

    public Task updateTask(Long id, String newTitle) {
        Task task = taskRepository.findById(id);
        if (task != null) {
            task.setTitle(newTitle);
            taskRepository.updateTask(task);
        }
        return task;
    }
    // Search tasks by title (case-insensitive, partial match)
    public List<Task> searchTasks(String query) {
        return taskRepository.searchByTitle(query);
    }
}

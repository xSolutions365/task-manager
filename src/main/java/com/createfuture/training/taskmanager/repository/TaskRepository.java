package com.createfuture.training.taskmanager.repository;

import com.createfuture.training.taskmanager.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Task> taskMapper = (rs, rowNum) -> new Task(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getBoolean("done")
    );

    public Task addTask(String title) {
        String sql = "INSERT INTO tasks (done, title) VALUES (?, ?)";
        jdbcTemplate.update(sql, false, title);
        return getLastInsertedTaskByTitle(title);
    }

    public Task getLastInsertedTaskByTitle(String title) {
        String sql = "SELECT * FROM tasks WHERE title = ? ORDER BY id DESC LIMIT 1";
        List<Task> tasks = jdbcTemplate.query(sql, taskMapper, title);
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(0);
    }

    public List<Task> findAll() {
        return jdbcTemplate.query("SELECT * FROM tasks", taskMapper);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }

    public List<Task> findTopN(int n) {
        String sql = "SELECT * FROM tasks LIMIT ?";
        return jdbcTemplate.query(sql, taskMapper, n);
    }

    public void reset() {
        jdbcTemplate.execute("DELETE FROM tasks");
    }

    public Task findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        List<Task> tasks = jdbcTemplate.query(sql, taskMapper, id);
        return tasks.isEmpty() ? null : tasks.get(0);
    }

    public int markDoneById(Long id) {
        String sql = "UPDATE tasks SET done = ? WHERE id = ?";
        return jdbcTemplate.update(sql, true, id);
    }

    public int updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, done = ? WHERE id = ?";
        return jdbcTemplate.update(sql, task.getTitle(), task.isDone(), task.getId());
    }

    // Search tasks by title (case-insensitive, partial match)
    public List<Task> searchByTitle(String query) {
        String sql = "SELECT * FROM tasks WHERE LOWER(title) LIKE ?";
        String pattern = "%" + query.toLowerCase() + "%";
        return jdbcTemplate.query(sql, taskMapper, pattern);
    }
}

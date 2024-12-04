package com.example.kaban.service;

import com.example.kaban.model.Task;
import com.example.kaban.model.User;
import com.example.kaban.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUserId(Integer userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task getTaskById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
    }

    public Task createTask(Integer userId, Task task) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());

        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new RuntimeException("Descrição não pode ser nula ou vazia");
        }
        if (task.getPriority() == null) {
            throw new RuntimeException("Prioridade não pode ser nula");
        }
        if (task.getStatus() == null) {
            task.setStatus(Task.Status.A_FAZER);
        }
        if (task.getSector() == null || task.getSector().isEmpty()) {
            throw new RuntimeException("Setor não pode ser nulo ou vazio");
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Integer taskId, Task updatedTask) {
        return taskRepository.findById(taskId).map(task -> {
            if (updatedTask.getDescription() != null && !updatedTask.getDescription().isEmpty()) {
                task.setDescription(updatedTask.getDescription());
            }
            if (updatedTask.getSector() != null && !updatedTask.getSector().isEmpty()) {
                task.setSector(updatedTask.getSector());
            }
            if (updatedTask.getPriority() != null) {
                task.setPriority(updatedTask.getPriority());
            }
            if (updatedTask.getStatus() != null) {
                task.setStatus(updatedTask.getStatus());
            } else if (task.getStatus() == null) {
                task.setStatus(Task.Status.A_FAZER);
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public Task updateStatus(Integer id, Task.Status status) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setStatus(status);
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    public Task updatePriority(Integer id, Task.Priority priority) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setPriority(priority);
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }
}

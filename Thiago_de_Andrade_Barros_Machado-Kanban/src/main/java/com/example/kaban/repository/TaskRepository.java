package com.example.kaban.repository;

import com.example.kaban.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(Integer userId);
}

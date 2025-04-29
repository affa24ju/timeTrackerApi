package com.timeTrackerAPI.timeTrackerAPI.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.timeTrackerAPI.timeTrackerAPI.models.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
}

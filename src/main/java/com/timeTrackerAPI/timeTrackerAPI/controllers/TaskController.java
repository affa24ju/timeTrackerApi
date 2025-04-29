package com.timeTrackerAPI.timeTrackerAPI.controllers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timeTrackerAPI.timeTrackerAPI.models.Task;
import com.timeTrackerAPI.timeTrackerAPI.repositories.TaskRepository;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    //Check in för task
    @PostMapping("/checkin")
    public Task checkIn(@RequestBody Task task){
        task.setStartTime(LocalDateTime.now());
        return taskRepository.save(task);
    }
    
    //Check out för ett task
    @PostMapping("/checkout/{id}")
    public Task checkOut(@PathVariable String id){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setEndTime(LocalDateTime.now());
            return taskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + id);
    }

}

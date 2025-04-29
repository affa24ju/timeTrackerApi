package com.timeTrackerAPI.timeTrackerAPI.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    
}

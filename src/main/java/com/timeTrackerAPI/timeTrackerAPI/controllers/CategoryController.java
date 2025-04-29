package com.timeTrackerAPI.timeTrackerAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timeTrackerAPI.timeTrackerAPI.models.Category;
import com.timeTrackerAPI.timeTrackerAPI.repositories.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
//För att tillåta frontend senare
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public Category createCategory(@RequestBody Category category){
        return categoryRepository.save(category);
    }
    
}

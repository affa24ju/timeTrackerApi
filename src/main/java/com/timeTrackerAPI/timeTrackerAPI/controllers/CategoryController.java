package com.timeTrackerAPI.timeTrackerAPI.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable String id, @RequestBody Category updatedCategory){
        return categoryRepository.findById(id)
            .map(existingCategory -> {
                existingCategory.setName(updatedCategory.getName());
                return categoryRepository.save(existingCategory);
            })
            .orElseThrow(() -> new RuntimeException("Kategori hittades inte med id: " + id));
    }
    
}

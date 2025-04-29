package com.timeTrackerAPI.timeTrackerAPI.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.timeTrackerAPI.timeTrackerAPI.models.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
    
}

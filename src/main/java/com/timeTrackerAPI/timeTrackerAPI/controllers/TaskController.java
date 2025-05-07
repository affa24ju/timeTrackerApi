package com.timeTrackerAPI.timeTrackerAPI.controllers;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timeTrackerAPI.timeTrackerAPI.models.Category;
import com.timeTrackerAPI.timeTrackerAPI.models.Task;
import com.timeTrackerAPI.timeTrackerAPI.models.WeeklyCategoryStat;
import com.timeTrackerAPI.timeTrackerAPI.repositories.CategoryRepository;
import com.timeTrackerAPI.timeTrackerAPI.repositories.TaskRepository;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    //Checka in för en uppgift
    @PostMapping("/checkin")
    public Task checkIn(@RequestBody Task task){
        task.setStartTime(LocalDateTime.now());
        return taskRepository.save(task);
    }
    
    //Checka ut för en uppgift, tar in id som pathvariable 
    @PostMapping("/checkout/{id}")
    public Task checkOut(@PathVariable String id){
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setEndTime(LocalDateTime.now());
            return taskRepository.save(task);
        }
        throw new RuntimeException("Uppgift hittades inte med id: " + id);
    }

    //Hämtar alla nuvarande veckans uppgiter
    @GetMapping("/week")
    public List<Task> getTasksThisWeek(){
        //Hämtar datum och klockslag
        LocalDateTime now = LocalDateTime.now();
        //Räknar ut början av vecka genom att ta dagens datum, flytta till den senaste mån/ samma dag, om 
        //det är redan måndag och sätter klockslag till 00:00
        LocalDateTime startOfWeek = now
            .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            .withHour(0)
            .withMinute(0);
        //Räknar ut slutet av vecka på samma sätt, sätter klockslag 23:59
            LocalDateTime endOfWeek = now
            .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY))
            .withHour(23)
            .withMinute(59);

        return taskRepository.findByStartTimeBetween(startOfWeek, endOfWeek);
    }

    //Visa statistik på hur många minuter per task denna veckan
    @GetMapping("/stats/week")
    public List<WeeklyCategoryStat> getWeeklyStats(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).withHour(0).withMinute(0);
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(23).withMinute(59);

        List<Task> tasks = taskRepository.findByStartTimeBetween(startOfWeek, endOfWeek);

        //Grupperar & summerar tid per kategori
        Map<String, Long> minutesPerCategoryId = tasks.stream()
            .filter(task -> task.getEndTime() != null) //Tar bara avslutade task
            .collect(Collectors.groupingBy(
                Task::getCategoryId,
                Collectors.summingLong(task ->
                    java.time.Duration.between(task.getStartTime(), task.getEndTime()).toMinutes()
                    )
            ));

        //Hämtar namn för varje kategoi-id
        return minutesPerCategoryId.entrySet().stream()
            .map(entry -> {
                String categoryId = entry.getKey();
                String categoryName = categoryRepository.findById(categoryId)
                    .map(Category::getName)
                    .orElse("Ökänd kategori");

                return new WeeklyCategoryStat(categoryName, entry.getValue());
            })
            .collect(Collectors.toList());

    }
    
    //Hämtar statistik för en vald vecka, t.ex. /api/tasks/stats/specificweek?year=2025&week=19 
    @GetMapping("/stats/specificweek")
    public List<WeeklyCategoryStat> getWeeklyStatsByYearAndWeek(@RequestParam int year, @RequestParam int week){
        //Skapar en kalender för att räkna ut veckans start & slut
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        //Startar med måndag i önskad vecka
        LocalDate startOfWeekDate = LocalDate
            .now()
            .withYear(year)
            .with(weekFields.weekOfYear(), week)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        
        //Slutar på söndag samma vecka
        LocalDate endOfWeekDate = startOfWeekDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //Gör om LocatDate till LocalDateTime för att inkludera tid
        LocalDateTime startOfWeek = startOfWeekDate.atStartOfDay();
        LocalDateTime endOfWeek = endOfWeekDate.atTime(23, 59);

        //Hämtar alla tasks som startade mellan mån 00:00 & sön 23:59
        List<Task> tasks = taskRepository.findByStartTimeBetween(startOfWeek, endOfWeek);

        //Grupperar & summerar antal minuter per kategori-id
        Map<String, Long> minutesPerCategoryId = tasks.stream()
            .filter(task -> task.getEndTime() != null) //filtrear bort tasks som inte är avslutad
            .collect(Collectors.groupingBy(
                Task::getCategoryId, //Gruperar på kategoriID
                Collectors.summingLong(task -> 
                    Duration.between(task.getStartTime(), task.getEndTime()).toMinutes() //summerar tid i minuter
                )
            ));

        //Konverterar från kategoriId till categoryName & bygger weeklyCategoryStat objekt per kategori
        return minutesPerCategoryId.entrySet().stream()
            .map(entry -> {
                String categoryId = entry.getKey();
                //Hämtar kategorinamn från category repository
                String categoryName = categoryRepository.findById(categoryId)
                    .map(Category::getName)
                    .orElse("Ökänd kategori");
                
                //Returnerar ett objekt med kategorynamn & minuter
                return new WeeklyCategoryStat(categoryName, entry.getValue());
            })
            .collect(Collectors.toList());
    }

}

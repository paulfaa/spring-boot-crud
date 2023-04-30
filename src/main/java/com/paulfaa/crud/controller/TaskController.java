package com.paulfaa.crud.controller;

import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.entity.TaskDto;
import com.paulfaa.crud.service.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        Task createdTask = taskService.saveTask(taskDto.toEntity());
        return new ResponseEntity<>(createdTask.toDto(), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> get(@PathVariable long id) {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task.toDto(), HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable long id, @RequestBody TaskDto taskDto) throws Exception {
        Task updatedTask = taskService.updateTask(id, taskDto.toEntity());
        return new ResponseEntity<>(updatedTask.toDto(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*@GetMapping("/tasks")
    public ResponseEntity<TaskDto> getAll(){
        Task task = taskService.getAllTasks();
        return new ResponseEntity<>(task.toDto(), HttpStatus.OK);
    }*/
}

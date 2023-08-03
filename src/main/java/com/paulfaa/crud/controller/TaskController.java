package com.paulfaa.crud.controller;

import com.google.common.collect.Lists;
import com.paulfaa.crud.entity.Status;
import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.entity.TaskDto;
import com.paulfaa.crud.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    private String SUCCESS_MESSAGE = "Successfully updated task %s";
    private String FAILURE_MESSAGE = "No task found for id %s";

    @PostMapping("/tasks")
    public ResponseEntity<Long> create(@RequestBody TaskDto taskDto) {
        Task task = new Task(taskDto.getTitle());
        task.setTaskStatus(Status.CREATED);
        task.setDescription(taskDto.getDescription());
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask.getId(), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> get(@PathVariable long id) {
        Optional<Task> optional = taskRepository.findById(id);
        return optional.map(task -> new ResponseEntity<>(task.toDto(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @RequestBody TaskDto taskDto) {
        List<String> validStatus = Arrays.stream(Status.values())
                .map(Enum::toString)
                .toList();
        if(!validStatus.contains(taskDto.getStatus())){
            return new ResponseEntity<>("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE", HttpStatus.BAD_REQUEST);
        }
        Optional<Task> optional = taskRepository.findById(id);
        if(optional.isEmpty()){
            return new ResponseEntity<>(String.format(FAILURE_MESSAGE, id) , HttpStatus.NO_CONTENT);
        }
        else{
            Task task = optional.get();
            task.setDescription(taskDto.getDescription());
            task.setTaskStatus(Status.valueOf(taskDto.getStatus()));
            Task updatedTask = taskRepository.save(task);
            return new ResponseEntity<>(String.format(SUCCESS_MESSAGE, updatedTask.getId()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Optional<Task> optional = taskRepository.findById(id);
        if(optional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/tasks")
    public List<TaskDto> getAll() {
        Iterable<Task> tasks = taskRepository.findAll();
        return Lists.newArrayList(tasks.iterator())
                .stream().map(Task::toDto).collect(Collectors.toList());
    }
}
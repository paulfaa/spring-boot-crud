package com.paulfaa.crud.controller;

import com.google.common.collect.Lists;
import com.paulfaa.crud.entity.Status;
import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.entity.TaskDto;
import com.paulfaa.crud.repository.TaskRepository;
import com.paulfaa.crud.service.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.paulfaa.crud.util.Util.findStatusByName;


@RestController
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/tasks")
    public ResponseEntity<Long> create(@RequestBody TaskDto taskDto) {
        Task task = new Task();
        task.setStatus(Status.CREATED);
        task.setDescription(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        taskService.saveTask(task);
        return new ResponseEntity<>(task.getId(), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> get(@PathVariable long id) {
        Optional<Task> optional = taskService.findById(id);
        return optional.map(task -> new ResponseEntity<>(task.toDto(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @RequestBody TaskDto taskDto) throws Exception {
        if (findStatusByName(taskDto.getStatus()) == null) {
            return new ResponseEntity<>("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE", HttpStatus.BAD_REQUEST);
        }
        Optional<Task> optional = taskService.findById(id);
        if(optional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            taskDto.setId(String.valueOf(id));
            taskService.updateTask(id, taskDto.toEntity());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Optional<Task> optional = taskService.findById(id);
        if(optional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/tasks")
    public List<TaskDto> getAll() {
        Iterable<Task> tasks = taskService.getAllTasks();
        return Lists.newArrayList(tasks.iterator())
                .stream().map(Task::toDto).collect(Collectors.toList());
    }
}

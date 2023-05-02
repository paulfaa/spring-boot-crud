package com.paulfaa.crud.controller;

import com.paulfaa.crud.entity.Status;
import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.entity.TaskDto;
import com.paulfaa.crud.repository.TaskRepository;
import com.paulfaa.crud.service.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;


@RestController
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        Task task = new Task();
        task.setStatus(Status.CREATED);
        task.setDescription(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        taskService.saveTask(task);
        return new ResponseEntity<>(task.toDto(), HttpStatus.CREATED);
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

    @GetMapping("/tasks")
    public List<TaskDto> getAll(){
        Iterable<Task> tasks = taskService.getAllTasks();
        List<TaskDto> list = new ArrayList<>();
        while(tasks.iterator().hasNext()){
            list.add(tasks.iterator().next().toDto());
        }
        return list;
    }
}

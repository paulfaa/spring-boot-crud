package com.paulfaa.crud.service;

import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        Optional<Task> result = taskRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task task) throws Exception {
        if (taskRepository.existsById(id)){
            return taskRepository.save(task);
        }
        else{
            throw new Exception("Task with id " + id + " does not exist");
        }
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}

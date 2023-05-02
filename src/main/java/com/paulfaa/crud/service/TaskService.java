package com.paulfaa.crud.service;

import com.paulfaa.crud.entity.Task;

import java.util.Optional;

public interface TaskService {

    Iterable<Task> getAllTasks();

    Task getTaskById(Long id);

    Task saveTask(Task task);

    Task updateTask(Long id, Task task) throws Exception;

    Optional<Task> findById(Long id);

    void deleteTask(Long id);
}


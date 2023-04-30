package com.paulfaa.crud.service;

import com.paulfaa.crud.entity.Task;

public interface TaskService {

    Iterable<Task> getAllTasks();

    Task getTaskById(Long id);

    Task saveTask(Task task);

    Task updateTask(Long id, Task task) throws Exception;

    void deleteTask(Long id);

}


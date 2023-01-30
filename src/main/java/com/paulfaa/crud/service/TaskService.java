package com.paulfaa.crud.service;

import com.paulfaa.crud.entity.Task;
import java.util.Optional;


public interface TaskService {

    Iterable<Task> getAllTasks();

    Task getTaskById(Integer id);

    Task saveTask(Task task);

    Task updateTask(Integer id, Task task);

    void deleteTask(Integer id);

}


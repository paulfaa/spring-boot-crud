package com.paulfaa.crud.repository;

import com.paulfaa.crud.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}

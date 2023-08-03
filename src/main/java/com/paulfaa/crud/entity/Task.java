package com.paulfaa.crud.entity;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Status status;

    public Task(String title) {
        this.title = title;
    }

    private Task(){};

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskDto toDto() {
        return new TaskDto(String.valueOf(id), title, description, String.valueOf(status));
    }
}

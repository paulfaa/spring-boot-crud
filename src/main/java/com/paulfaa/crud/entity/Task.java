package com.paulfaa.crud.entity;

import org.springframework.data.annotation.Id;
public class Task {
    @Id
    private Long id;
    private String title;
    private String description;
    private Status status;

    public Task(Long id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskDto toDto() {
        return new TaskDto(String.valueOf(id), title, description, String.valueOf(status));
    }
}

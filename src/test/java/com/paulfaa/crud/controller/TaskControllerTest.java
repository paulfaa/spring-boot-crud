package com.paulfaa.crud.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paulfaa.crud.entity.Status;
import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.entity.TaskDto;
import com.paulfaa.crud.repository.TaskRepository;
import com.paulfaa.crud.service.TaskServiceImpl;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @MockBean
    TaskRepository mockTaskRepository;

    @MockBean
    TaskServiceImpl taskService;

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    public TaskController taskController;

    Gson gson = new Gson();

    private final Task task1 = new Task(1L, "Title", "Description", Status.CREATED);
    private final Task task2 = new Task(1L, "Updated title", "Updated Description", Status.APPROVED);

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testPostTaskSuccess() throws Exception { //id is not getting set automatically by @GeneratedValue in tests
        //Arrange
        Mockito.when(taskService.saveTask(any())).thenReturn(task1);

        //Act
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(task1.toDto()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("1"));
    }

    @Test
    public void testGetTaskSuccess() throws Exception {
        //Arrange
        Mockito.when(taskService.findById(any())).thenReturn(Optional.of(task1));

        //Act
        mockMvc.perform(get("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(gson.toJson(task1.toDto())));
    }

    @Test
    public void testGetTaskInvalidId() throws Exception {
        //Arrange
        Mockito.when(taskService.findById(any())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(get("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void testPutTaskSuccess() throws Exception {
        //Arrange
        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task1));
        Mockito.when(taskService.updateTask(1L, task2)).thenReturn(task2);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put("title", "Updated Title");
        jsonObject.put("description", "Updated Description");
        jsonObject.put("status", "APPROVED");

        //Act
        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void testPutTaskInvalidStatus() throws Exception {
        //Arrange
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put("title", "Updated Title");
        jsonObject.put("description", "Updated Description");
        jsonObject.put("status", "INVALID");

        //Act
        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(jsonObject))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE"));
    }

    @Test
    public void testPutTaskInvalidId() throws Exception {
        //Arrange
        //Mockito.when(taskService.updateTask(1L, task2)).thenReturn(task2);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put("title", "Updated Title");
        jsonObject.put("description", "Updated Description");
        jsonObject.put("status", "APPROVED");

        //Act
        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(jsonObject))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void deleteTaskSuccess() throws Exception {
        //Arrange
        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task1));
        //Mockito.when(taskService.deleteTask(1L)).thenReturn(void);

        //Act
        mockMvc.perform(delete("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void deleteTaskInvalidId() throws Exception {
        //Arrange
        Mockito.when(taskService.getTaskById(1L)).thenReturn(null);

        //Act
        mockMvc.perform(delete("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void getAllTasks() throws Exception {
        //Arrange
        ArrayList<Task> mockTasks = new ArrayList<>(Arrays.asList(task1, task2));
        ArrayList<TaskDto> expectedResponse = new ArrayList<>(Arrays.asList(task1.toDto(), task2.toDto()));
        Mockito.when(taskService.getAllTasks()).thenReturn(mockTasks);

        //Act
        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(gson.toJson(expectedResponse)));
    }

    @Test
    public void getAllTasksNoTasks() throws Exception {
        //Arrange
        ArrayList<Task> emptyList = new ArrayList<>();
        Mockito.when(taskService.getAllTasks()).thenReturn(emptyList);

        //Act
        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
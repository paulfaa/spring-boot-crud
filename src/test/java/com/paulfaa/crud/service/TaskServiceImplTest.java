package com.paulfaa.crud.service;

import com.google.common.collect.Iterables;
import com.paulfaa.crud.entity.Status;
import com.paulfaa.crud.entity.Task;
import com.paulfaa.crud.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    TaskRepository mockTaskRepository;

    @InjectMocks
    public TaskServiceImpl taskService;

    public ArrayList<Task> mockTasks;

    public Task mockTask = new Task(1L, "Name", "Description", Status.CREATED);


    @BeforeEach
    void setUp() {
        this.mockTasks = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetAllTasks() {
        //Arrange
        mockTasks.add(new Task(1L, "Name", "Description", Status.CREATED));
        when(mockTaskRepository.findAll()).thenReturn(mockTasks);

        //Act
        Iterable<Task> result = taskService.getAllTasks();

        //Assert
        Assertions.assertEquals(mockTasks, result);
    }

    @Test
    public void testGetAllTasksEmpty() {
        //Arrange
        when(mockTaskRepository.findAll()).thenReturn(mockTasks);

        //Act
        Iterable<Task> result = taskService.getAllTasks();

        //Assert
        Assertions.assertEquals(0, Iterables.size(result));
        Assertions.assertTrue(mockTasks.isEmpty());
    }

    @Test
    public void testGetTaskById() {
        //Arrange
        Optional<Task> expectedTask = Optional.of(mockTask);
        when(mockTaskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        //Act
        Optional<Task> result = taskService.findById(1L);

        //Assert
        Assertions.assertEquals(expectedTask, result);
    }

    @Test
    public void testGetTaskByIdNotExisting() {
        //Arrange
        when(mockTaskRepository.findById(999L)).thenReturn(Optional.empty());

        //Act
        Optional<Task> result = taskService.findById(999L);

        //Assert
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void updateTask() throws Exception {
        //Arrange
        Task updatedTask = new Task(1L, "New Name", "New Description", Status.APPROVED);
        when(mockTaskRepository.existsById(any())).thenReturn(true);

        //Act
        Task result = taskService.updateTask(1L, mockTask);

        //Assert
        verify(mockTaskRepository, times(1)).save(mockTask);
    }

    @Test
    public void updateTaskNotExisting() throws Exception {
        //Arrange
        String expectedMessage = "Task with id 1 does not exist";

        //Act
        Exception exception = assertThrows(Exception.class, () -> {
            taskService.updateTask(1L, mockTask);
        });
        String actualMessage = exception.getMessage();

        //Assert
        assertEquals(expectedMessage, actualMessage);
    }
}
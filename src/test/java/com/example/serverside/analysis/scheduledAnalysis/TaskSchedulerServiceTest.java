package com.example.serverside.analysis.scheduledAnalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskSchedulerServiceTest {

    @Mock
    private ScheduledTaskRepository scheduledTaskRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TaskSchedulerService taskSchedulerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteTasks_NoTasksDue() {
        when(scheduledTaskRepository.findByScheduleTimeBefore(any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        taskSchedulerService.executeTasks();

        verify(scheduledTaskRepository, times(1)).findByScheduleTimeBefore(any(LocalDateTime.class));
        verify(restTemplate, never()).getForObject(anyString(), eq(Void.class));
        verify(scheduledTaskRepository, never()).delete(any(ScheduledTask.class));
    }

    @Test
    void testExecuteTasks_WithDueTasks() {
        ScheduledTask task = new ScheduledTask();
        task.setId("1");
        task.setEndpoint("/test");
        task.setUsername("user");
        task.setRepo("repo");
        task.setCommitId("commit");
        task.setPath("path");

        when(scheduledTaskRepository.findByScheduleTimeBefore(any(LocalDateTime.class))).thenReturn(List.of(task));
        when(restTemplate.getForObject(anyString(), eq(Void.class))).thenReturn(null);

        taskSchedulerService.executeTasks();

        verify(scheduledTaskRepository, times(1)).findByScheduleTimeBefore(any(LocalDateTime.class));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Void.class));
        verify(scheduledTaskRepository, times(1)).delete(task);
    }

    @Test
    void testExecuteTasks_TaskExecutionFails() {
        ScheduledTask task = new ScheduledTask();
        task.setId("1");
        task.setEndpoint("/test");
        task.setUsername("user");
        task.setRepo("repo");
        task.setCommitId("commit");
        task.setPath("path");
        task.setShouldPostComment(true);

        when(scheduledTaskRepository.findByScheduleTimeBefore(any(LocalDateTime.class))).thenReturn(List.of(task));
        doThrow(new RuntimeException("Simulated exception")).when(restTemplate).getForObject(anyString(), eq(Void.class));

        taskSchedulerService.executeTasks();

        verify(scheduledTaskRepository, times(1)).findByScheduleTimeBefore(any(LocalDateTime.class));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Void.class));

        verify(scheduledTaskRepository, never()).delete(task);
    }

}

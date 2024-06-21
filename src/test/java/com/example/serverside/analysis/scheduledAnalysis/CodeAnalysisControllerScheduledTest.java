package com.example.serverside.analysis.scheduledAnalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CodeAnalysisControllerScheduledTest {

    @Mock
    private ScheduledTaskRepository scheduledTaskRepository;

    @InjectMocks
    private CodeAnalysisControllerScheduled codeAnalysisControllerScheduled;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleAnalysis_Success() {
        ScheduledTask taskDetails = new ScheduledTask();
        String scheduleTime = "2023-12-18T12:00:00";
        LocalDateTime parsedScheduleTime = LocalDateTime.parse(scheduleTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        taskDetails.setScheduleTime(parsedScheduleTime);

        when(scheduledTaskRepository.save(taskDetails)).thenReturn(taskDetails);

        ResponseEntity<String> responseEntity = codeAnalysisControllerScheduled.scheduleAnalysis(taskDetails);

        verify(scheduledTaskRepository, times(1)).save(taskDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedScheduleTime = parsedScheduleTime.format(formatter);
        String expected = "Analysis scheduled for: " + formattedScheduleTime;

        assertEquals(expected, responseEntity.getBody());
    }
}

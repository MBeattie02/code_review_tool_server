package com.example.serverside.analysis.scheduledAnalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduledTaskTest {

    private ScheduledTask scheduledTask;
    private final LocalDateTime testTime = LocalDateTime.now();
    private final Map<String, String> testParams = new HashMap<>();

    @BeforeEach
    void setUp() {
        scheduledTask = new ScheduledTask();

        // Populate the testParams map for testing
        testParams.put("param1", "value1");
        testParams.put("param2", "value2");
    }

    @Test
    void testGettersAndSetters() {
        scheduledTask.setId("testId");
        scheduledTask.setUsername("testUsername");
        scheduledTask.setRepo("testRepo");
        scheduledTask.setCommitId("testCommitId");
        scheduledTask.setPath("testPath");
        scheduledTask.setEndpoint("testEndpoint");
        scheduledTask.setScheduleTime(testTime);
        scheduledTask.setAdditionalParams(testParams);

        assertEquals("testId", scheduledTask.getId());
        assertEquals("testUsername", scheduledTask.getUsername());
        assertEquals("testRepo", scheduledTask.getRepo());
        assertEquals("testCommitId", scheduledTask.getCommitId());
        assertEquals("testPath", scheduledTask.getPath());
        assertEquals("testEndpoint", scheduledTask.getEndpoint());
        assertEquals(testTime, scheduledTask.getScheduleTime());
        assertEquals(testParams, scheduledTask.getAdditionalParams());
    }
}

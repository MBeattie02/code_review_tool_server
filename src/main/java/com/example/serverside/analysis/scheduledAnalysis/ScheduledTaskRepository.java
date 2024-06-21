package com.example.serverside.analysis.scheduledAnalysis;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB repository interface for ScheduledTask documents.
 * This interface extends MongoRepository to provide custom query methods for ScheduledTask documents.
 */
public interface ScheduledTaskRepository extends MongoRepository<ScheduledTask, String> {

    /**
     * Finds all ScheduledTask documents where the schedule time is before the specified time.
     * This method is used to retrieve tasks that are due for execution.
     *
     * @param time The LocalDateTime against which to check the schedule time of tasks.
     * @return A list of ScheduledTask documents that are scheduled before the specified time.
     */
    List<ScheduledTask> findByScheduleTimeBefore(LocalDateTime time);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opsysfinal;

import java.util.ArrayList;
import java.util.List;

/**
 * File: ScheduleQueueReport.java
 Author: Leith McCombs
 Date: Jun 25, 2016
 Purpose: describe purpose of class here
 */
public class ScheduleQueueReport {
    private final long totalTimeProcessingQueue, queueStartTime, queueEndTime; 
    private final String queueName;
    private static List<ThreadData> runs = new ArrayList<>();
    
    /**
     * Generates a report containing detailed performance informations about a 
     * {@code ScheduleQueue} instance and the threads it ran. Use the {@code toString()} 
     * method to view the data.
     * @param q the {@code ScheduleQueue} to prepare a report on.
     * @param queueType a descriptor of the type of queue in question.
     */
    public ScheduleQueueReport(ScheduleQueue q, String queueType) {
        totalTimeProcessingQueue = q.getTotalTime();
        queueStartTime = q.getStartTime();
        queueEndTime = q.getEndTime();
        this.queueName = queueType;
    }
    
    /**
     * Adds the data from a specific job to the static list of ThreadData objects
     * used by the report.
     * @param data a ThreadData object containing performance data for a thread.
     */
    public static void addRun(ThreadData data) {
        runs.add(data);
    }

    /**
     * Removes all data from previous jobs.
     */
    public static void clearRuns() {
        ScheduleQueueReport.runs = new ArrayList<>();
    }
    
    /**
     * Calculates and displays standard performance metrics for threads run from 
     * a scheduling queue in an easily readable form.
     * @return a summary of performance data for a set of jobs run in
     * a scheduling queue.
     */
    @Override
    public String toString() {
        String s = "Results for scheduling queue " + queueName + " with "
                + runs.size() + "threads:\n";
        for (ThreadData run : runs) {
            s += "    " + run.type.substring(run.type.lastIndexOf(".") + 1) + " " + run.name 
                    + "\n\tQueue start to job start: "
                    + (run.jobStartTime - queueStartTime) + "ms,\n\tqueue start to job end: "
                    + (run.jobEndTime - queueStartTime) + "ms,\n\tjob start to job end: "
                    + run.jobStartToJobEndTime + "ms,\n\tactive execution time: "
                    + run.activeExecutionTime + "ms,\n\ttotal wait times between time slices: "
                    + run.jobInternalWaitTime + "ms,\n\ttotal wait time: "
                    + (run.jobInternalWaitTime + run.jobStartTime - queueStartTime)
                    + "ms,\n\tspeed rank: " + run.speedRank + "\n";
        }

        s += "Total time for queue: " + (queueEndTime - queueStartTime) + "ms\n";
        return s;
    }
    
    /**
     * Calculates and formats standard performance metrics for threads run from 
     * a scheduling queue into Comma Separated Text.
     * @return a summary of performance data for a set of jobs run in
     * a scheduling queue.
     */
    public String toCSV() {
        String s = "Results for scheduling queue " + queueName + " with "
                + runs.size() + "threads:\n"
                + "RunType, ThreadName, QueueStartToJobStart, QueueStartToJobEnd, "
                + "JobStartToJobEnd, ActiveExecTime, InternalWaitTime, "
                + "TotalWaitTime, SpeedRank, DataValue\n";
        for (ThreadData run : runs) {
            s += "    " + run.type.substring(run.type.lastIndexOf(".") + 1) + ", " + run.name 
                    + ", " + (run.jobStartTime - queueStartTime) + ", " 
                    + (run.jobEndTime - queueStartTime) + ", "
                    + run.jobStartToJobEndTime + ", "
                    + run.activeExecutionTime + ", "
                    + run.jobInternalWaitTime + ", "
                    + (run.jobInternalWaitTime + run.jobStartTime - queueStartTime)
                    + ", " + run.speedRank 
                    + "' " + run.value + "\n";
        }

        s += "Total time for queue: " + (queueEndTime - queueStartTime) + "ms\n";
        return s;
    }    
}

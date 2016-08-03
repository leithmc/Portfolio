/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opsysfinal;

/**
 * File: ThreadData.java
 * Author: Leith McCombs
 * Date: Jun 26, 2016
 * Purpose: describe purpose of class here
 */
public class ThreadData {
    long activeExecutionTime, jobStartToJobEndTime, jobInternalWaitTime
            ,jobEndTime, jobStartTime;
    String name, type;
    int speedRank;
    long totalTimeProcessingQueue, queueStartTime; 
    String queueName;
    int threadCount;
    double value;
    
    public ThreadData(TrackableThread t) {
        this.activeExecutionTime = t.getActiveExecutionTime();
        this.jobStartToJobEndTime = t.getTotalElapsedTime();
        this.jobInternalWaitTime = jobStartToJobEndTime - activeExecutionTime;
        this.jobStartTime = t.getJobStartTime();
        this.jobEndTime = t.getJobEndTime();
        this.name = t.getName();
        this.type = t.getClass().toString();
        this.speedRank = t.getSpeedRank();
        this.value = t.getDataValue();
    }        
}

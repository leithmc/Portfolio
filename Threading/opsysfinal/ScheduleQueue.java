/**
 * File: ScheduleQueue.java
 * Author: Leith McCombs
 * Date: Jun 25, 2016
 * Purpose: Defines shared functionality between the different schedule queue 
 * implementations.
 */
package opsysfinal;

import java.util.LinkedList;
import java.util.List;

/**
 * Defines the shared functionality between the different scheduling queues, 
 * including time tracking and report generation.
 */
public abstract class ScheduleQueue extends LinkedList<TrackableThread> {
    private boolean trackToCompletion = true;
    private long startTime = 0, endTime=0, totalTime = 0;
    private String name;
    private ScheduleQueueReport results = null;

    /**
     * Use this constructor to override the default value of trackToCompletion,
     * which is {@code true}.
     * @param trackToCompletion sets whether the ScheduleQueue instance should
     * remain open to track the total time to completion of all jobs in the 
     * queue. If {@code true}, the queue will stay open to completion and return 
     * a ScheduleQueueReport with performance data. Otherwise, the queue closes
     * when the active thread handling is complete and returns {@code null}.
     * When overriding this constructor, you must call this constructor to 
     * supply the name of the queue.     * 
     */
    public ScheduleQueue(boolean trackToCompletion, String name) {
        this.trackToCompletion = trackToCompletion;
        this.name = name;
    }
    
    /**
     * Created a schedule queue with default values that stays open until the 
     * last job is complete and then returns a ScheduleQueueReport object
     * containing performance data for the jobs. When overriding this constructor,
     * you must call this constructor to supply the name of the queue.
     */
    public ScheduleQueue(String name) { this.name = name; }
    
    /**
     * Starts a queue of scheduled jobs and tracks total time to completion. 
     * Implement the processJobs() method to specify the algorithm for how
     * the threads are scheduled, started, and managed.
     * @return a ScheduleQueueReport object containing performance information
     * about the threads that are run, including active execution time, wait 
     * time, and total time from start to finish.
     * @throws InterruptedException 
     */   
    public ScheduleQueueReport Start() throws InterruptedException {
        // Add a reference to each thread in bkpList so its data is still 
        // available if removed from the main queue
        
        // Clear previous data from the report
        ScheduleQueueReport.clearRuns();
        
        // Start the timer for queue
        startTime = System.currentTimeMillis();
        
        // Start and manage the jobs in the queue
        processJobs();
        
        if (trackToCompletion) {
            // Wait for the last thread to close and get its result data
            for (TrackableThread t : this) {
                t.join();
            }
            // Update totalTime
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;

            // Generate a report and exit
            results = new ScheduleQueueReport(this, name);
            return results;
        }
        else {  
            // Not waiting, so set to -1 and return null
            totalTime = -1;
            return null;
        } 
    }
    
    /**
     * @return the total time, in milliseconds, between starting the queue and
     * completing the last job. If trackToCompletion is turned off in the 
     * constructor, returns -1.
     */
    public final long getTotalTime() { return totalTime; }

    /**
     * Override this method to implement how the threads are scheduled, started, 
     * and managed.
     */
    abstract void processJobs();

    /**
     * @return the time, in milliseconds, when the queue began processing jobs.
     */
    public final long getStartTime() {
        return startTime;
    }

    /**
     * @return the time, in milliseconds, when the last job in the queue 
     * finished processing.
     */    
    public long getEndTime() {
        return endTime;
    }
    
    /**
     * Returns 
     * @return the arbitrary name of the queue, set in the constructor.
     */
    public String getName() { return name; }

    ScheduleQueueReport getResults() {
        return results;
    }    
}

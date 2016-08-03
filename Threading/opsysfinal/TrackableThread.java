/**
 * File: TrackableThread.java
 * Author: Leith McCombs
 * Date: Jun 25, 2016
 * Purpose: Abstract class for threads that implements time tracking.
 */
package opsysfinal;



/**
 * Extends the Thread class to automatically track total elapsed time and total
 * active execution time. Also includes pause() and unPause() methods to safely
 * suspend and resume the thread.
 */
public abstract class TrackableThread extends Thread{
    private long totalElapsedTime=0, activeExecutionTime=0, sessionStartTime=0
            , jobStartTime=0, jobEndTime=0;
    
    /**
     * Starts the current thread, does whatever work is specified in the
     * doTaskOnRun() method, and records the total elapsed time between
     * start and completion in this.getTotalElapsedTime().
     * If showTimes is set to {@code true} in the constructor, writes the total
     * elapsed time to the console at the end of the run. 
     * This method cannot be overridden. To perform work in the thread, override
     * the doTaskOnRun() method instead.
     */
    @Override
    public final void run() {
        // Set the start timers
        totalElapsedTime=0; activeExecutionTime=0;
        jobStartTime = sessionStartTime = System.currentTimeMillis();
        
        // Do the work to be timed
        doTaskOnRun();
        
        // Stop the timers
        jobEndTime = System.currentTimeMillis();
        activeExecutionTime += (jobEndTime - sessionStartTime);
        totalElapsedTime = jobEndTime - jobStartTime;
        
        // Send performance data to the report for the run
        ScheduleQueueReport.addRun(new ThreadData(this));
    }

    /**
     * Override this method to specify any actual work to be done by the thread.
     * The time expended in the work will be tracked in the getTotalElapsedTime()
     * and getActiveExecutionTime methods.
     */
    abstract void doTaskOnRun();
    
    /**
     * Used by SJF queues to evaluate the proposed speed of the thread.
     * @return an integer value that represents the estimated execution time of 
     * the thread. The higher the value, the shorter the time compared to other 
     * threads.
     */       
    abstract int getSpeedRank();
    
    /**
     * @return the elapsed time used by the active thread. This information must be
     * supplied by the calling thread through the addTime(long) method.
     */   
    public long getTotalElapsedTime(){
        return totalElapsedTime;
    }
 
    /**
     * @return the total time spent in active execution of the thread.
     */ 
    public long getActiveExecutionTime() {
        return activeExecutionTime;
    }

    /**
     * @return the time, in milliseconds, when this thread began execution.
     */
    public long getJobStartTime() {
        return jobStartTime;
    }

    /**
     * @return the time, in milliseconds, when this thread finished execution.
     */
    public long getJobEndTime() {
        return jobEndTime;
    }
    
    /**
     * Called by the parent queue to declare a job start time other than when
     * the {@code Run()} method is first invoked.
     * @param jobStartTime the start time, in milliseconds, to declare.
     */
    public void setJobStartTime(long jobStartTime) {
        this.jobStartTime = jobStartTime;
    }
    
    /**
     * Pauses execution of this thread.
     */    
    public void pause(){
        activeExecutionTime += (System.currentTimeMillis() - sessionStartTime);   
        synchronized (this) {
            this.suspend();
        }
    }
    
    /**
     * Resumes execution of the thread from a paused state.
     */
    public final void unPause(){
        sessionStartTime = System.currentTimeMillis();
        synchronized (this) {
            this.resume();
        }
    }       
    
    /**
     * Use this method to verify data on threads in a scheduler.
     * @return A data value produced by the thread.
     */
    public abstract Double getDataValue();
}

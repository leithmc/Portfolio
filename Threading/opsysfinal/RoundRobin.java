/**
 * File: RoundRobin.java
 * Author: Leith McCombs
 * Date: Jun 22, 2016
 * Purpose: Defines a Round Robin scheduling queue
 */

package opsysfinal;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines a Round Robin scheduling queue, which allots each job in the queue a
 * specified time slice in which to process before being moved to the back of
 * the queue and waiting for its next turn.
 */
public class RoundRobin extends ScheduleQueue{
    private long timeSlice = 10;
    
    /**
     * Creates a new RoundRobin queue instance.
     * @param timeSlice specifies the amount of time, in milliseconds, to allot
     * to each thread before it gets moved to the back of the queue.
     * @param name the name of the queue to appear on the performance report.
     */
    public RoundRobin(long timeSlice, String name) {
        super(name);
        this.timeSlice = timeSlice;
    }
    
    /**
     * @return the amount of time, in milliseconds, to allot to each thread 
     * before it gets moved to the back of the queue.
     */
    public long getTimeSlice() { return timeSlice; }

    /**
     * Called by the RoundRobin.Start() method. Starts the threads one at a 
     * time, allowing each thread to continue for {@code timeSlice) milliseconds
     * before moving to the back of the queue to wait for another turn. 
     * Continues until all threads have exited.
     */
    @Override
    void processJobs() {
        // Start and immediately pause each thread so they are all in the same
        // state for the pause and resume loop
        for (TrackableThread t : this) {
            t.setJobStartTime(0);
            t.start();
            t.pause();
        }
        
        // Cycle through the queue alotting time slices until each thread dies
        while (!this.isEmpty()) {
            // Grab the thread at the front of the queue
            TrackableThread t = removeFirst(); 
            
            // If this is the first time unPausing this thread, set the jobStarTime
            if (t.getJobStartTime() == 0) t.setJobStartTime(System.currentTimeMillis());

            // Start the thread
            t.unPause();

            // Wait until time slice expires or the thread dies
            //try { t.join(timeSlice);
            //} catch (InterruptedException ex) {}
            for (int i = 0; i < timeSlice; i++) {
                if (t.isAlive()) try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {}
            }
            
            // If the thread is still alive, pause it and move to back of queue
            if (t.isAlive()) {
                t.pause();
                addLast(t);
            }
        }
    }       
}

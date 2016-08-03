/**
 * File: FCFS.java
 * Author: Leith McCombs
 * Date: Jun 3, 2016
 * Purpose: describe purpose of class here
 */

package opsysfinal;
/**
 * Defines a first-come-first-served scheduling queue.
 * @author leith_000
 */
public class FCFS extends ScheduleQueue{  // LinkedList is queue implementation
    
    /**
     * Creates a new instance of a First-Come-First-Served scheduling queue.
     * @param name the name of the queue to appear on the performance report.
     */
    public FCFS(String name) {super(name);}
    
    /**
     * Removes the thread from the front of the queue, runs it to completion, 
     * and repeats until the queue is empty.
     * */
    @Override
    void processJobs() {
        while (!this.isEmpty()) {
            Thread t = this.removeFirst(); // Grab the thread at the front of the queue
            t.start();          // Start the thread
            try { t.join(); }   // Wait for the thread to complete
            catch (InterruptedException e) {}
        }
    }
}

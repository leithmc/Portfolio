/**
 * File: SJF.java
 * Author: Leith McCombs
 * Date: Jun 22, 2016
 * Purpose: A shortest-job-first queue
 */

package opsysfinal;

/**
 * Defines a shortest-job-first scheduling queue. Job length is arbitrarily
 * determined by the speedRank of the incoming jobs, with the higher speedRank
 * indicating the job that is expected to complete faster.
 */
public class SJF extends FCFS{

    public SJF(String name) {
        super(name);
    }
    /**
     * Adds the specified TrackableThread to the queue in the position
     * immediately after the last thread of equal or higher speedRank.
     * @param t the thread to add.
     */
    @Override
    public void addLast(TrackableThread t) {
        int speedRank = t.getSpeedRank();
        int index = size();
        while (index > 0 && speedRank > get(index - 1).getSpeedRank()) {
            index--;
        }
        add(index, t);
    }    
}
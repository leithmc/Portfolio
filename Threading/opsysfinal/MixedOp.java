/**
 * File: MixedOp.java
 * Author: Leith McCombs
 * Date: Jun 22, 2016
 * Purpose: Runs the tasks of both CompOp and IoOp, but runs the CompOp task
 * 1000 times for each run of the IoOp task.
 */

package opsysfinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Defines a thread that executes a combination of CPU-intensive and 
 * IO-intensive activities.
 */
public class MixedOp extends TrackableThread {
    private final int startValue;
    private final String phrase;
    private final int iterations;
    private final int speedRank = 2;
    private double d = 1;

    /**
     * Creates a new MixedOp thread object.
     * @param startValue an arbitrary number to use in mathematical calculations.
     * @param phrase a short phrase to write repeatedly to a file.
     * @param iterations the number of times to perform the IO-intensive 
     * operation. The CPU-intensive operation will be performed 1000 times as
     * often.
     */
    public MixedOp(int startValue, String phrase, int iterations) {
        this.startValue = startValue;
        this.phrase = phrase;
        this.iterations = iterations;
    }

    /**
     * Called by MixedOp.Run(). Performs a CPU-intensive task {@code iterations}
     * * 1000 times and then performs an IO-intensive task {@code iterations} 
     * times.
     */
    @Override
    void doTaskOnRun() {
        d = 1;   // Create a variable in memory once
        for (int i = 1; i < iterations * 1000; i++) {  // Do lots of math on the variable
            d += Math.log((Math.PI / i) * Math.sqrt((double) startValue));
        }        
        
        String s = "";   // Create a string object in memory
        for (int i = 0; i < iterations; i++) {
            s += phrase; // Make the string longer each time
            try {       // Write the string to a file
                BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                writer.write(s);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Thread " + this.getName() 
                        + "failed to write to file after " + i 
                        + "successful writes. " + ex.getMessage() 
                        + "\nAborting thread.");
                return;
            }
        }
    }

    /**
     * @return the speed rank of the thread, which is used by the SJF queue to
     * determine queue order.
     */
    @Override
    int getSpeedRank() { return speedRank; }

    @Override
    public Double getDataValue() { return d; }
}

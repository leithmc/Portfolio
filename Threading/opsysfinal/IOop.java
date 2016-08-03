/**
 * File: IOop.java
 * Author: Leith McCombs
 * Date: May 26, 2016
 * Purpose: Thread class to run an IO-intensive operation
 */

package opsysfinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Defines a thread class that executes an IO-intensive operation.
  */
public class IOop extends TrackableThread {
    private final String phrase;
    private final int iterations;
    private final int speedRank = 1;

    /**
     * Creates a new instance of an IOop thread, which when started, writes
     * {@code phrase} to a file {@code iterations} times.
     * @param phrase the phrase to write.
     * @param iterations how many times to write it.
     */
    public IOop(String phrase, int iterations) {
        this.phrase = phrase;
        this.iterations = iterations;
    }

    /**
     * Called by IOop.Run(). Writes {@code phrase} to a file {@code iterations} 
     * times and then exits.
     */
    @Override
    void doTaskOnRun() {
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
    public Double getDataValue() {return 0.0;}
}


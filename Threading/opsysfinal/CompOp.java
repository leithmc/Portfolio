/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsysfinal;

/**
 * File: CompOp.java Author: Leith McCombs Date: May 26, 2016 Purpose: describe
 * purpose of class here
 */
public class CompOp extends TrackableThread {
    private final int startValue;
    private final int iterations;
    private final int speedRank = 3;
    private double d = 1; 

    /**
     * Creates a new instance of a CompOp thread, which when started, performs a 
     * complex mathematical calculation {@code iterations} times and then exits.
     * @param startValue a numeric seed value for the calculation.
     * @param iterations how many times to calculate it.
     */
    public CompOp(int startValue, int iterations) {
        this.startValue = startValue;
        this.iterations = iterations;
    }

    /**
     * Called by CompOp.Run(). Performs a complex mathematical calculation
     * {@code iterations} times and then exits.
     */    
    @Override
    void doTaskOnRun() {
       for (int i = 1; i < iterations; i++) {  // Do lots of math on the variable
            d += Math.log((Math.PI / i) * Math.sqrt((double) startValue));
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opsysfinal;

import java.util.LinkedList;
import java.util.List;

/**
 * File: OpSysFinal
 * Author Leith McCombs
 * Date: Jun 22, 2016
 * Purpose: Compare the performance of FCFS, SJF, and RoundRobin schedulers
 */
public class OpSysFinal {
    public static void main(String[] args) throws InterruptedException {
        // Start the jobs and display reports
        ScheduleQueueReport results;
        System.out.println("***STARTING JOBS***");
        FCFS fcfs = new FCFS("FCFS");
        fillQueue(fcfs);
        results = fcfs.Start();
        System.out.println(results.toCSV());
        SJF sjf = new SJF("SJF");
        fillQueue(sjf);
        results =  sjf.Start();
        System.out.println(results.toCSV());
        RoundRobin rr = new RoundRobin(100L, "RoundRobin");
        fillQueue(rr);
        results = rr.Start();
        System.out.println(results.toCSV());

    }
    
    static void fillQueue(ScheduleQueue q) {
        for (int i = 0; i < 10; i++) {
            q.addLast(new CompOp(10, 20000000));
            q.addLast(new IOop("Hello, world.\n", 2000));
            q.addLast(new MixedOp(1, "I'm all mixed up.\n", 1000));
        }
    }
}

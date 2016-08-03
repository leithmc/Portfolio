/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package caveofterror;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * File: Job.java
 * Author: Leith McCombs
 * Date: Feb 11, 2016
 * Purpose: describe purpose of class here
 */
// j:<index>:<name>:<creature index>:<time>[:<required artifact type>:<number>]*
class Job extends GameElement implements Runnable {
    private final long duration;
    private final Creature creature;
    HashMap<String, Integer> requirements;
    private final JProgressBar pb = new JProgressBar();
    JButton btnGo   = new JButton ("Stop");
    JButton btnCancel = new JButton ("Cancel");
    private final JPanel resourcePane = new JPanel();
    private String reqs;
    JLabel lblReqs = new JLabel();
    Status status = Status.SUSPENDED;
    boolean cancelled = false;
    private boolean goFlag = true;
    //List<Lock> locks = new LinkedList<>();
    final List<Artifact> artifacts = new ArrayList<>();

    enum Status {RUNNING, SUSPENDED, WAITING, DONE};
    JPanel display = new JPanel();
    public Job(int id, String name, Creature creature, long duration, 
            HashMap<String, Integer> requirements, Container hostPane ) {
        super(id, "job", name, creature.getId());
        resourcePane.setLayout(new BoxLayout(resourcePane, BoxLayout.PAGE_AXIS));
        this.requirements = requirements;
        this.duration = duration;
        this.creature = creature;
    

    }

    JPanel getDisplay() { 
        pb.setStringPainted(true);
        display.setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
        JLabel nameTag = new JLabel(creature.getName() + ": " + name);
        nameTag.setVerticalAlignment(SwingConstants.BOTTOM);
        top.add(nameTag);
        reqs = "Requires: ";
        for (String s : requirements.keySet()) {
            reqs += requirements.get(s) + " " + s;
            reqs += (requirements.get(s) > 1) ?  "s, " : ", ";
        }
        if (reqs.endsWith(", ")) reqs = reqs.substring(0, reqs.length() - 2);
        lblReqs.setText("        " + reqs);
        top.add(lblReqs);
        middle.add(pb);
        middle.add(btnGo);
        middle.add(btnCancel);
        btnGo.addActionListener (e -> toggleGoFlag());
        btnCancel.addActionListener (e -> cancelJob ()); 

        bottom.add(resourcePane);
//        bottom.add(new JLabel("******************************"));
//        bottom.add(new JLabel("      "));
        display.add(top, BorderLayout.NORTH);
        display.add(middle, BorderLayout.WEST);
        display.add(bottom, BorderLayout.SOUTH);
        return display;
    }
    
    void addResource(Artifact a) {
        if (!artifacts.contains(a)) artifacts.add(a);
        resourcePane.add(new JLabel(a.toString()));
        resourcePane.validate();
    }
    
    void removeResource(Artifact a) {
        if (artifacts.contains(a)) artifacts.remove(a);
        for (Component c : resourcePane.getComponents()) {
            if (c instanceof JLabel && ((JLabel)c).getText().equals(a.toString())) {
                resourcePane.remove(c);
                break;
            }
        }
    }

    @Override
    public void run() {
        // Wait for creature to be available
        synchronized(creature.party) {
            while (creature.isBusy) {
                showStatus(status.WAITING);
                try { creature.party.wait(); }
                catch (InterruptedException ex) {}
            }
            creature.isBusy = true;
        }

        long elapsedTime = 0;
        long xDur = duration * 10; // Count by 10ths to make progress bar smoother
        while (elapsedTime < xDur && !cancelled) {
            if(goFlag) {
                showStatus(status.RUNNING);
                try {
                    Thread.sleep(100);
                    elapsedTime++;
                    if (cancelled) break;
                }
                catch (InterruptedException ex) {}
                //elapsedTime++;
                int progress = (int) ((elapsedTime * 100 / xDur));
                pb.setValue(progress);
            }
            else showStatus(status.SUSPENDED);
        }
        // Job is done
        pb.setValue(100);
        showStatus(status.DONE);
        synchronized (creature.party) {
            creature.isBusy = false;
            creature.party.notifyAll();
        }
    }

    private void showStatus(Status status) {
        switch (status) {
            case RUNNING:
                pb.setBackground(Color.GREEN);
                btnGo.setText("Pause");
                btnGo.setEnabled(true);
                break;
            case SUSPENDED:
                pb.setBackground(Color.yellow);
                btnGo.setText("Resume");
                btnGo.setEnabled(true);
                break;
            case WAITING:
                pb.setBackground(Color.orange);
                btnGo.setText("Waiting...");
                btnGo.setEnabled(false);
                break;
            case DONE:
                pb.setBackground(Color.red);
                if (cancelled) btnGo.setText("Cancelled");
                else btnGo.setText("Done");
                btnGo.setEnabled(false);
                btnCancel.setVisible(false);
                synchronized(creature.party) {
////                    HashMap<Artifact, Job> map = getParty().allocations;
////                    for (Artifact a : map.keySet()) { 
////                        if (map.get(a).equals(this)) {
////                            map.remove(a);
////                            map.put(a, null);
////                            creature.party.notifyAll();
////                        }
////                    }
                    for (Artifact a : artifacts) creature.party.addResource(a);
                }
                resourcePane.removeAll();
                break;
            default:
                throw new AssertionError(status.name());            
        }
    }
    
    public Creature getCreature() { return creature; }    
    public Party getParty() { return creature.getParty(); }
    private void toggleGoFlag() { goFlag = !goFlag; }
    private void cancelJob() { cancelled = true; }
} // end class Job

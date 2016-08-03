package caveofterror;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

/**
 * File: Party.java
 * Author: Leith McCombs
 * Date: Jan 22, 2016
 * Purpose: Models a party of intrepid adventurers
 */
class Party extends GameElement implements TreeNode{
    private final ArrayList<Creature> creatures;
    private final List<Artifact> resourcePool = new ArrayList<>();
    private final JobQueue waitingJobs;
    private final JPanel display = new JPanel();
    private final JPanel leftPane = new JPanel();
    private final JPanel rightPane = new JPanel();
    private final JPanel resourcePane = new JPanel();
    private final HashMap<String , Integer> artifactTally = new HashMap<>();
    final HashMap<Artifact, Job> allocations = new HashMap<>();
    private boolean jobsWaiting;
    boolean needsRefresh = false;
    
    Party(int id, String name) {
        super(id, "", name, 0);
        this.creatures = new ArrayList<>();
        this.waitingJobs = new JobQueue();
        synchronized(this) {
            for (Artifact a : getArtifacts()) 
                for (Job job : getJobs())
                    if (!job.artifacts.contains(a)) resourcePool.add(a);
        }
        // Arrange the GUI components
        display.setLayout(new BorderLayout());
        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
        resourcePane.setLayout(new BoxLayout(resourcePane, BoxLayout.PAGE_AXIS));
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));

        Thread t = new Thread(waitingJobs, "JobQueue");
        t.start();                
    }

    public JPanel getDisplay() {
        display.removeAll();
        resourcePane.removeAll();
        for (Artifact a : resourcePool) {
            JLabel l = new JLabel(a.toString());
            resourcePane.add(l);
        }
        resourcePane.add(new JLabel("                                                                  "));
        resourcePane.validate();
        leftPane.removeAll();
        leftPane.add(new JLabel("Party: " + name));
        leftPane.add(new JLabel("Available artifacts:"));
        leftPane.add(resourcePane);
        //rightPane.removeAll();
        for (Creature c : creatures) for (Job job : c.getJobs()) rightPane.add(job.getDisplay());
        display.add(leftPane, BorderLayout.WEST);
        display.add(rightPane, BorderLayout.CENTER);
        needsRefresh = true;
        return display;
    }
    
    void refresh() {
        
    }
    
    // Adds an artifact to the shared pool. Call from synchronized code.
    void addResource(Artifact a) {
        if (!resourcePool.contains(a)) resourcePool.add(a);
        resourcePane.add(new JLabel(a.toString()));
        resourcePane.validate();
    }
    
    // Removes an artifact from the shared pool. Call from synchronized code.
    void removeResource(Artifact a) {
        if (resourcePool.contains(a)) resourcePool.remove(a);
        for (Component c : resourcePane.getComponents()) {
            if (c instanceof JLabel && ((JLabel)c).getText().equals(a.toString())) {
                resourcePane.remove(c);
                break;
            }
        }
    }

    public LinkedList<Artifact> getArtifacts() {
        LinkedList<Artifact> artifacts = new LinkedList<>();
        for (Creature c : creatures) artifacts.addAll(c.getArtifacts());
        return artifacts;
    }

    public LinkedList<Job> getJobs() {
        return waitingJobs;
    }
    
    public void addJob(Job job) { 
        waitingJobs.add(job);
        rightPane.add(job.getDisplay());
    }
    public synchronized boolean startNextJob() {
        Job job = null;
        jobsWaiting = (waitingJobs.size() > 0);
        //synchronized (this) {
        while (!jobsWaiting) { try {wait();} catch (InterruptedException ex) {}}
        try {
            job = waitingJobs.removeFirst();
            if (waitingJobs.size() == 0) jobsWaiting = false;
        }
        catch (NoSuchElementException ex) {
            if (job != null && !waitingJobs.contains(job)) waitingJobs.add(job);
            return false;
        }
        notifyAll();
        ///}
        
        //System.out.println("Job started: " + job.name);
        
        List<Artifact> itemsHeld = new ArrayList<>();
        //List<Lock> locks = new LinkedList<>();
        //synchronized (this) {        
        for (String req : job.requirements.keySet()) {
            // For each type of artifact required, try to lock enough of them to start
            //int added = 0; 
            int needed = job.requirements.get(req);
            ArrayList<Artifact> sub = new ArrayList<>();
            sub = (ArrayList<Artifact>) getArtifacts().stream().filter(a ->
                    a.type.equals(req) && !a.isInUse()).collect(Collectors.toList());
                
            //}
//            for (Artifact a : getArtifacts()) if (a.type.equals(req) && !a.isInUse()) sub.add(a);
//            while (added < needed && !sub.isEmpty()) {
//                Artifact a = sub.remove();
////                if (a.lock.tryLock()) {
////                    itemsHeld.add(a);
////                    added++;
////                }
//                if (!a.isInUse()) a.use(job);
//            }
            
            // If an artifact was unavailable, release and go to back of queue
            if (sub.size() < needed) {
                waitingJobs.add(job);
                return false;
            }// {            
//                for (Lock lock : locks) lock.unlock();
//                synchronized (waitingJobs) {waitingJobs.add(job);}
                //return false; 
            itemsHeld.addAll(sub);
        
        }
            
        // Reassign the artifacts to their new owners
        for (Artifact a : itemsHeld) {
//            if (creatures.stream().filter(c -> c.getArtifacts().contains(a))
//                    .findAny().get().getArtifacts().remove(a)) {
//                job.getCreature().getArtifacts().add(a);
//            }
//            allocations.put(a, job);
            job.addResource(a);
            removeResource(a);
        }
        resourcePane.validate();
        leftPane.validate();
        rightPane.validate();
        display.validate();

        // Everything is in place. Now start the job.          
        String longName = job.getCreature().getName() + ": " + job.getName();
        (new Thread(job, longName)).start();
        
        return true;
    }
    
    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    // This version sorts the list before returning it
    public void sortCreatures(SortBy sortBy) {
        switch (sortBy) {
            case NAME:
                creatures.sort((Creature a, Creature b) 
                        -> a.getName().compareTo(b.getName()));
                break;
            case ID:
                creatures.sort((Creature a, Creature b) 
                        -> a.getId() - b.getId());
                break;
            case TYPE:
                creatures.sort((Creature a, Creature b) 
                        -> a.getItemType().compareTo(b.getItemType()));
                break;
            case EMPATHY:
                creatures.sort((Creature a, Creature b) 
                        -> a.getEmpathy()- b.getEmpathy());
                break;
            case FEAR:
                creatures.sort((Creature a, Creature b) 
                        -> a.getFear()- b.getFear());
                break;
            case CARRY:
                creatures.sort((Creature a, Creature b) 
                        -> (int) (a.getCarry() - b.getCarry()));
                break;
            default:
                throw new AssertionError(sortBy.name());
        }
    }
        
    public boolean addMember(Creature member) {
        return creatures.add(member);
    }

    // Return a string representation in the same format as the desired text input
    @Override
    public String toString() {
        return "" + super.getId() + " : " + super.getName();
    }

//    void startQueue() {
//        Thread t = new Thread(waitingJobs, "Job queue for party " + name);
//        t.start();
//    }

    private class JobQueue extends LinkedList<Job> implements Runnable{
        private static final long serialVersionUID = 1L;
        @Override
        public void run() {
            while (true) {
                while (this.isEmpty()) 
                    try {Thread.sleep(100);} catch (InterruptedException ex) {}
                startNextJob();
            }
        }
    }
    
    public enum SortBy {
        NAME, ID, TYPE, EMPATHY, FEAR, CARRY;
    }
    
    // TreeNode implementation
    @Override
    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) creatures.get(childIndex);
    }
    public int getChildCount()          {return creatures.size();}
    public TreeNode getParent()         {return this.getParent();}
    public int getIndex(TreeNode node)  {return creatures.indexOf(node);}
    public boolean getAllowsChildren()  {return true;}
    public boolean isLeaf()             {return (creatures.isEmpty());}
    public Enumeration children()       {return (Enumeration) creatures.iterator();}   
}

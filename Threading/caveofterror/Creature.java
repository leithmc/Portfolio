package caveofterror;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;

/**
 * File: Creature.java
 * Author: Leith McCombs
 * Date: Jan 22, 2016
 * Purpose: Model a creature.
 */
class Creature extends GameElement implements TreeNode {
    final private int empathy, fear;
    final private double carry;
    private final ArrayList<Artifact> artifacts;
    private final ArrayList<Treasure> treasures;
    final Integer partyID;
    boolean isBusy = false; 
    List<Job> jobs;
    Party party = null;

    Creature(int id, String type, String name, int party, int empathy, int fear, double carry) {
        super(id, type, name, party);
        this.jobs = new ArrayList<>();
        this.partyID = this.owner;
        this.empathy = empathy;
        this.fear = fear;
        this.carry = carry;
        this.artifacts = new ArrayList<>();
        this.treasures = new ArrayList<>();
    }

    
//    public void addJob(Job j) {
//        jobs.add(j);
//    }
//    
//    public boolean removeJob(Job j) {
//        return jobs.remove(j);
//    }

    public List<Job> getJobs() { return jobs; }
    public double getCarry() { return carry; }
    public int getEmpathy() { return empathy; }
    public int getFear() { return fear; }    
    public Party getParty() { return party; }
    public void setParty(Party p) { party = p; }    
    public ArrayList<Artifact> getArtifacts() { return artifacts; }
    public ArrayList<Treasure> getTreasures() { return treasures; }

    // Sorted version of getter sort the list before returning it
    public void sortTreasures(SortBy sortBy) {
        switch (sortBy){
            case TYPE:
                treasures.sort((Treasure a, Treasure b) 
                        -> a.getItemType().compareTo(b.getItemType()));
                break;
            case WEIGHT:
                treasures.sort((Treasure a, Treasure b) 
                        -> (int) (a.getWeight()- b.getWeight()));
                break;
            case VALUE:
                treasures.sort((Treasure a, Treasure b) 
                        -> (int) (a.getValue()- b.getValue()));
                break;
            default:
                //throw new AssertionError(sortBy.name());            
        }
    }
    // Return a string representation in the same format as the desired text input
    @Override
    public String toString() {
        return "" + super.getId() + " : " + super.getItemType() + " : " 
                + super.getName() + " : " + super.getOwner() + " : " 
                + empathy + " : " + fear + " : " + carry;
    }

    private List<GameElement> allItems() {
        List<GameElement> list = (List<GameElement>) artifacts.clone();
        list.addAll((List<GameElement>) treasures.clone());
        return list;
    }
    
    // TreeNode implementation
    @Override
    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) allItems().get(childIndex);
    }
    public int getChildCount()          {return allItems().size();}
    public TreeNode getParent()         {return this.getParent();}
    public int getIndex(TreeNode node)  {return allItems().indexOf(node);}
    public boolean getAllowsChildren()  {return true;}
    public boolean isLeaf()             {return (allItems().isEmpty());}
    public Enumeration children()       {return (Enumeration) allItems().iterator();}    
    
    public enum SortBy {
        TYPE, WEIGHT, VALUE;
    }

    enum Status {RUNNING, SUSPENDED, WAITING, DONE};
}



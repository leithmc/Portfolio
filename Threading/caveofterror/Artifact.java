package caveofterror;

import java.util.Enumeration;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.tree.TreeNode;

/**
 * File: Artifact.java
 * Author: Leith McCombs
 * Date: Jan 22, 2016
 * Purpose: Describes a magical artifact
 */
public class Artifact extends GameElement implements TreeNode {
    Lock lock = new ReentrantLock();
    private boolean inUse = false;
    Artifact(int id, String type, int creature, String name) {
        super(id, type, name, creature);
    }

    // Call this to claim an artifact from synchronized code after waiting on Job.isInUse
    public void use(Job job) throws java.lang.Exception {
        if (inUse) throw Exception("Artifact already in use: " + this.toString());
        inUse = true;   
        // Move to the correct creature
        for (Creature c : job.getParty().getCreatures()) {
            c.getArtifacts().remove(this);
            break;
        }
        job.getCreature().getArtifacts().add(this);
        job.addResource(this);
        job.getParty().removeResource(this);
        job.getParty().notifyAll();
    }
    
    public boolean isInUse() {return inUse;}
    
    
    // Return a string representation in the same format as the desired text input
    @Override
    public String toString() {
        return super.getItemType() + " : " + super.getId() + " : " + super.getName();
    }
    
    // TreeNode implementation
    @Override
    public TreeNode getChildAt(int childIndex) { return null; }
    public int getChildCount()          {return 0;}
    public TreeNode getParent()         {return this.getParent();}
    public int getIndex(TreeNode node)  {return -1;}
    public boolean getAllowsChildren()  {return false;}
    public boolean isLeaf()             {return true;}
    public Enumeration children()       {return null;}     

    private Exception Exception(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

package caveofterror;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * File: Treasure.java
 * Author: Leith McCombs
 * Date: Jan 22, 2016
 * Purpose: Describes shiny stuff
 */
public class Treasure extends GameElement implements TreeNode {
    double weight, value;

    Treasure(int id, String type, int creature, double weight, double value) {
        super(id, type, "", creature);
        this.weight = weight;
        this.value = value;   
    }

    public double getWeight() {
        return weight;
    }

    public double getValue() {
        return value;
    }

    // Return a string representation in the same format as the desired text input
    @Override
    public String toString() {
        String s = "" + super.getId() + " : " + super.getItemType() + " : " 
                + super.getOwner() + " : " + weight + " : " + value;
        String n = super.getName();
        if (!n.equals("")) s += " : " + n;
        return s;
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
}

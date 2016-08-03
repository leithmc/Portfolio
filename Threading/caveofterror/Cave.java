package caveofterror;

import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.swing.tree.TreeNode;

/**
 * File: Cave.java
 * Author: Leith McCombs
 * Date: Feb 4, 2016
 * Purpose: Models a cave. 
 */
class Cave implements TreeNode{
    private final ArrayList<Party> parties;
    private final GameList orphans;
    private final List<String> errors;    
    private final Container jobHost;
    public Cave(File f, Container jobView) throws FileNotFoundException {
        parties = new ArrayList<>();
        errors = new ArrayList<>();
        orphans = new GameList();
        jobHost = jobView;
        readFile(f);
    }

    // Used by the Search button. Since the point was to search by name, 
    // this only applies to elements that have names.
    public GameElement searchByName(String name) {
        for (Party p : parties) {
            if (p.getName().equalsIgnoreCase(name)) return p;
            for (Creature c : p.getCreatures()) {
                if (c.getName().equalsIgnoreCase(name)) return c;
            }
        }
        for (GameElement g : orphans) 
            if (g.getName().equalsIgnoreCase(name)) return g;
        return null;
    }
    
    // Used by the Search button.
    public GameElement searchById(Integer id) {
        for (Party p : parties) {
            if (p.getId() == id) return p;
            for (Creature c : p.getCreatures()) {
                if (c.getId() == id) return c;
                for (Artifact a : c.getArtifacts()) 
                    if (a.getId() == id) return a;
                for (Treasure t : c.getTreasures()) 
                    if (t.getId() == id) return t;                
            }
        }
        for (GameElement g : orphans) 
            if (g.getId() == id) return g;
        return null;
    }
    
    private void readFile(File file ) throws FileNotFoundException {
        HashMap<Integer, Party> hmParties = new HashMap<>();
        HashMap<Integer, Creature> hmCreatures = new HashMap<>();
        HashMap<Integer, Treasure> hmTreasures = new HashMap<>();
        HashMap<Integer, Artifact> hmArtifacts = new HashMap<>();
        HashMap<Integer, Job> hmJobs = new HashMap<>();
        // Read the file into the data array.
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            // Read the next line and split into temporary array
            String line = sc.nextLine().trim();
            // Regex checks for: letter, whitespace:whitespace, number, whitespace:whitespace, whatever
            String regEx = "\\w\\s*:\\s*\\d+\\s*:\\s*\\w+.*";
            // Validate line format, skip if it doesn't match
            if (!line.matches(regEx)) continue;
            //  Split into fields, removing excess whitespeace
            String[] fields = line.split("\\s*:\\s*");
            // Add elements by type
            switch (fields[0].toLowerCase()) {
                case "p":
                    Party p = addParty(fields);
                    if (p != null) {
                        hmParties.put(p.getId(), p);
                        //p.startQueue();
                    }
                    else errors.add("Could not load party: " + line);                     
                    break;
                case "c":
                    Creature c = addCreature(fields);
                    if (c != null) hmCreatures.put(c.getId(), c);
                    else errors.add("Could not load creature: " + line);
                    break;
                case "t":
                    Treasure t = addTreasure(fields);
                    if (t != null) hmTreasures.put(t.getId(), t);
                    else errors.add("Could not load treasure: " + line);
                    break;
                case "a":
                    Artifact a = addArtifact(fields);
                    if (a != null) hmArtifacts.put(a.getId(), a);
                    else errors.add("Could not load artifact: " + line);
                    break;
                case "j":
                    Job j = addJob(fields, hmCreatures);
                    if (j!= null) hmJobs.put(j.getId(), j);
                    else errors.add("Could not load job: " + line);
                    break;
                default:
                    errors.add("String format unrecognized: " + line);
            }
        } // end while
        // After creating the main HashMaps, add child nodes to their parents collections
        hmCreatures.values().stream().forEach((c) -> {
            Integer i = c.getOwner();
            if (hmParties.containsKey(i)) {
                hmParties.get(i).getCreatures().add(c);
                c.setParty(hmParties.get(i));
            }
            else this.orphans.add((GameElement) c);
        });
        hmTreasures.values().stream().forEach((t) -> {
            Integer i = t.getOwner();
            if (hmCreatures.containsKey(i)) hmCreatures.get(i).getTreasures().add(t);
            else this.orphans.add((GameElement) t);
        });
        hmArtifacts.values().stream().forEach((a) -> {
            Integer i = a.getOwner();
            if (hmCreatures.containsKey(i)) {
                hmCreatures.get(i).getArtifacts().add(a);
                hmParties.get(hmCreatures.get(i).partyID).getArtifacts().add(a);
            }
            else this.orphans.add(a);
        });      
        
        // May not need this one
        hmJobs.values().stream().forEach((j) -> {
            Integer i = j.getOwner();
            if (hmCreatures.containsKey(i)) {
                hmCreatures.get(i).getJobs().add(j);
                Party party = hmCreatures.get(i).getParty();
                party.addJob(j);
            }
        });                
        hmParties.values().stream().forEach((p) -> { 
            this.parties.add(p);
            //p.buildArtifactList();
        });        
    }
    
    // Create a Party object from text fields then add to hashmaps and to the cave
    private Party addParty(String[] fields) {
        int id;
        try {
            id = Integer.parseInt(fields[1]);
            String name = fields[2];
            Party p = new Party(id, name);
            return p;
        }
        catch(Exception ex) { return null; }
    }
    
    // Create a Creature object from text fields
    private Creature addCreature(String[] fields) {
        int id, party, empathy, fear;
        double carry;
        String type, name;
        try {
            id = Integer.parseInt(fields[1]);
            type = fields[2];
            name = fields[3];
            party = Integer.parseInt(fields[4]);
            empathy = Integer.parseInt(fields[5]);
            fear = Integer.parseInt(fields[6]);
            carry = Double.parseDouble(fields[7]);
            Creature c = new Creature(id, type, name, party, empathy, fear, carry);
            return c;
        }
        catch(Exception ex) { return null; }        
    }
    
    // Create a Treasure object from the text fields
    private Treasure addTreasure(String[] fields) {
        int id, creature;
        double weight, value;
        String type;    
        try {
            id = Integer.parseInt(fields[1]);
            type = fields[2];
            creature = Integer.parseInt(fields[3]);
            weight = Double.parseDouble(fields[4]);
            value = Double.parseDouble(fields[5]);
            Treasure t = new Treasure(id, type, creature, weight, value);
            return t;
        }
        catch(Exception ex) { return null; }
    }
    
    // Create new artifact from input text
    private Artifact addArtifact(String[] fields) {
        int id, creature;
        String type, name = "";    
        try {
            id = Integer.parseInt(fields[1]);
            type = fields[2];
            creature = Integer.parseInt(fields[3]);
            if (fields.length > 4) name = fields[4];
            Artifact a = new Artifact(id, type, creature, name);
            return a;
        }
        catch(Exception ex) { return null; }
    }   
    
    private Job addJob(String[] fields, HashMap<Integer, Creature> creatures) {
        int id;
        Creature creature;
        long duration;
        String name;
        HashMap<String, Integer> requirements = new HashMap<>();
        try {
            id = Integer.parseInt(fields[1]);
            name = fields[2];
            creature = creatures.get(Integer.parseInt(fields[3]));
            duration = Long.parseLong(fields[4]);
            for (int i = 5; i < fields.length - 1; i++) {
                requirements.put(fields[i++], Integer.parseInt(fields[i]));
            }
            return (new Job(id, name, creature, duration, requirements, jobHost));
        }
        catch(Exception ex) { return null; }        
    }

    // This is what shows up in the tree view
    public String toString()  {
        return "*The Cave of Terror*";
    }
    
    // Standard getters and setters        
    public ArrayList<Party> getParties() { return parties; }
    public ArrayList<GameElement> getOrphans() { return orphans; }    
    public List<String> getErrors() { return errors; }    
    public void clearErrors(List<String> errors) { this.errors.clear(); }

    // Getters for the search functions
    List<Artifact> getArtifacts() {
        List<Artifact> artifacts = new ArrayList<>();
        for (Party p : parties) {
            for (Creature c : p.getCreatures()) {
                for (Artifact a : c.getArtifacts()) artifacts.add(a);
            }
        }       
        return artifacts;
    }

    List<Treasure> getTreasures() {
        List<Treasure> treasures = new ArrayList<>();
        for (Party p : parties) {
            for (Creature c : p.getCreatures()) {
                for (Treasure t : c.getTreasures()) treasures.add(t);
            }
        }           
        return treasures;
    }

    List<Creature> getCreatures() {
        List<Creature> critters = new ArrayList<>();
        for (Party p : parties) {
            for (Creature c : p.getCreatures()) critters.add(c);
        }        
        return critters;
    }

    // TreeNode implementation
    @Override
    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) parties.get(childIndex);
    }
    public int getChildCount()          {return parties.size();}
    public TreeNode getParent()         {return null;}
    public int getIndex(TreeNode node)  {return parties.indexOf(node);}
    public boolean getAllowsChildren()  {return true;}
    public boolean isLeaf()             {return (parties.isEmpty());}
    public Enumeration children()       {return (Enumeration) parties;}

    //Create a formatted TOC-like visual structure to disply the complete data set
    public String toDeepString()  {
        // The word "cave" is alone at 0 indent
        String output = "Cave\n";
        // Each partyID has a line at 20 indent
        for (Party party : parties) {
            output += buildMargin(20) + party.getName() + "\n";  
            // Party members appear on the lines after their partyID
            output = party.getCreatures().stream().map((c) -> 
                    getCreatureString(c)).reduce(output, String::concat) + "\n";
        }
        // Take care of the orphan elements
        for (GameElement g : orphans ) {
            if (g instanceof Creature) output += "\n" + getCreatureString((Creature) g);
            else if (g instanceof Artifact) output += "\n" + getArtifactString((Artifact) g);
            else if (g instanceof Treasure) output += "\n" + getTreasureString((Treasure) g);
        }
        return output;
    }
    
    // For the TOC representation
    private String getCreatureString(Creature c) {
        // Each creature gets an indent of 60
        String output = buildMargin(40) + c.getName() + " -- empathy: " + c.getEmpathy()
                + ", fear: " + c.getFear() + ", carry: " + c.getCarry() + "\n";
        // Treasures and artifacts go on the lines after their owners, with indent of 90
        for (Treasure t : c.getTreasures()) output += getTreasureString(t);
        for (Artifact a : c.getArtifacts()) output += getArtifactString(a);
        return output;
    }
    
    // For the TOC representation
    private String getTreasureString(Treasure t) {
        return buildMargin(60) + t.getItemType() + " : " + t.getValue()+ "\n";          
    }
    
    private String getArtifactString(Artifact a) {
        return buildMargin(60) + a.getItemType() + " : " + a.getName() + "\n";          
    }

    // build an indent of the specified length
    private String buildMargin(int m) {
        String output = "";
        for (int i = 0; i < m; i++) output += " ";
        return output;
    }    

    private class GameList extends ArrayList<GameElement> implements TreeNode{
        private static final long serialVersionUID = 1L;
        List<GameElement> orphans;
        public GameList() {orphans = new ArrayList<>();}
        // TreeNode implementation
        public int getChildCount()          {return orphans.size();}
        public TreeNode getParent()         {return this.getParent();}
        public int getIndex(TreeNode node)  {return orphans.indexOf(node);}
        public boolean getAllowsChildren()  {return true;}
        public boolean isLeaf()             {return (orphans.isEmpty());}
        public Enumeration children()       {return (Enumeration) orphans.iterator();} 
        public TreeNode getChildAt(int childIndex) {return (TreeNode) orphans.get(childIndex);}
    }
}

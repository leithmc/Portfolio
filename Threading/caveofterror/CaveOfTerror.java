package caveofterror;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

/**
 * File: CaveOfTerror.java
 * Author: Leith McCombs
 * Date: Feb4, 2016
 * Purpose: Contains the UI for the cave game
 */
public class CaveOfTerror extends JFrame{
    private static final long serialVersionUID = 1L;
    private Cave cave;
    // Define UI elements
    private static CaveOfTerror frame;
    private final JButton btnLoadFile = new JButton("Load Data File");
    private final JButton btnShowTree = new JButton("Tree view");
    private final JButton btnShowText = new JButton("Text view");
    private final JButton btnShowJobs = new JButton("Job view");    
    private final JTextArea textView = new JTextArea();
    private final JFileChooser chooser = new JFileChooser(".");
    private final JLabel lblTxtSearch = new JLabel("Search by name or numeric id");
    private final JLabel lblType = new JLabel("Select item type");
    private final JLabel lblItem = new JLabel("Select item");
    private final JTextField txtSearch = new JTextField("");
    private final String[] itemTypes = {"Party", "Creature", "Treasure", "Artifact"} ;
    private final JComboBox<String> cmbItemTypes = new JComboBox<>(itemTypes);
    private final JComboBox<String> cmbItems = new JComboBox<>();
    private final JButton btnSearch = new JButton("Search");
    private final JPanel header = new JPanel();
    private final JPanel controls =  new JPanel();     
    private JScrollPane scroller;
    private final JLabel lblSortCreatures = new JLabel("Sort members by:");
    private final JComboBox<Party.SortBy> cmbSortCreatures 
            = new JComboBox<>(Party.SortBy.values());
    private final JLabel lblTreasureSort = new JLabel("Sort treasures by:");    
    private final JComboBox<Creature.SortBy> cmbSortTreasures 
            = new JComboBox<>(Creature.SortBy.values());
    private JTree tree;
    JPanel jobView = new JPanel();
    
    public CaveOfTerror() {
        // Set caption
        super("The Cave of **TERROR**");
        
        // Set layout
        setLayout(new BorderLayout());
        header.setLayout(new BorderLayout());
        controls.setLayout(new GridLayout(6, 3, 12, 4));
        jobView.setLayout(new BoxLayout(jobView, BoxLayout.PAGE_AXIS));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Hide UI elements that aren't ready to show yet
        btnShowTree.setEnabled(false);
        btnShowText.setEnabled(false);
        btnShowJobs.setEnabled(false);
        cmbItemTypes.setEnabled(false);
        cmbItems.setEnabled(false);
        btnSearch.setEnabled(false);
        cmbSortCreatures.setEnabled(false);
        cmbSortTreasures.setEnabled(false);
        
        // Add active UI to controls panel -- top row
        controls.add(btnLoadFile);
        controls.add(lblSortCreatures);
        controls.add(lblTreasureSort);
        // row 2
        controls.add(lblTxtSearch);
        controls.add(cmbSortCreatures);
        controls.add(cmbSortTreasures);
        // row 3
        controls.add(txtSearch);
        controls.add(lblType);
        controls.add(lblItem);
        // row 4
        controls.add(btnSearch);        
        controls.add(cmbItemTypes);
        controls.add(cmbItems);
        // row 5
        controls.add(btnShowTree);
        controls.add(btnShowText);
        controls.add(btnShowJobs);
        
        // Add the controls to the frame
        header.add(controls, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        addEventHandlers();
    }

    public static void main(String[] args) {
        // Create a frame instance and make it visible
        frame = new CaveOfTerror();
        frame.setVisible(true);
    }

    private void addEventHandlers() {
        // Handler for Load button
        btnLoadFile.addActionListener((ActionEvent e) -> { loadFile(); });
        
        // Handlers for the ShowX buttons
        btnShowTree.addActionListener((ActionEvent e2) -> { 
            showData(btnShowTree, cave);
        });
        btnShowText.addActionListener((ActionEvent e2) -> { 
            textView.setText(cave.toDeepString());
            showData(btnShowText, null );
        });
        btnShowJobs.addActionListener((ActionEvent e2) -> { showData(btnShowJobs, null); });
        btnSearch.addActionListener((ActionEvent e3) -> { search(); });
        cmbItemTypes.addActionListener((ActionEvent e4) -> { showItems(); });
        cmbItems.addActionListener((ActionEvent e5) -> { 
            showSelectedItem(cmbItems.getSelectedIndex()); });
        
        // Sort all creatures by selected criteria and display cave
        cmbSortCreatures.addActionListener((ActionEvent e6) -> {
            for (Party p : cave.getParties()) p.sortCreatures(
                    (Party.SortBy) cmbSortCreatures.getSelectedItem());            
            showData(btnShowTree, cave);
        });
        
        // Sort all treasures by selected criterion and display cave
        cmbSortTreasures.addActionListener((ActionEvent e7) -> {
           for (Party p : cave.getParties())
               for (Creature c : p.getCreatures())
                   c.sortTreasures( 
                           (Creature.SortBy) cmbSortTreasures.getSelectedItem());
           showData(btnShowTree, cave);              
        });
    }
    
    private void showData(JButton button, TreeNode node) {
        // Recreate the tree in the new order if needed
        if (node != null) tree = new JTree(node);

        // Get whatever was there before out of the way
        if (scroller != null) remove(scroller);

        // update the button status
        JButton[] buttons = {btnShowTree, btnShowText, btnShowJobs};
        for (JButton b : buttons ) b.setEnabled(!b.equals(button));

        // Put the right view in the scroller and add it to the frame
        Component[] views = {tree, textView, jobView};
        for (int i = 0; i < 3; i++)
            if (button.getText().equals(buttons[i].getText()))
                scroller = new JScrollPane(views[i]);
        add(scroller, BorderLayout.CENTER);
        frame.validate();
    }

    private void loadFile() {
        // Open file chooser. If user clicks OK, do stuff.
        if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                // Load the main data structure from the selected file
                cave = new Cave(f, jobView);

                // Display errors if there are any
                List<String> errors = cave.getErrors();
                if (errors.size() > 0) {
                    textView.setText(errors.toString().replaceAll(",", "\n"));                        
                    showData(btnShowText, null);
                }

                // Show jobs if present, tree otherwise
                try { 
                    for (Party p : cave.getParties()) jobView.add(p.getDisplay());
                    jobView.getComponent(0);  // not sure what this was for or if still needed
                    showData(btnShowJobs, null);
                }
                catch(ArrayIndexOutOfBoundsException ex) {
                    showData(btnShowTree,cave);
                }

                // Enable the rest of he UI
                btnShowTree.setEnabled(true);
                cmbSortCreatures.setEnabled(true);
                cmbSortTreasures.setEnabled(true);
                cmbItemTypes.setEnabled(true);
                cmbItems.setEnabled(true);
                btnSearch.setEnabled(true);
            }
            catch (IOException ex){
                textView.setText("Could not load file " + f.getAbsolutePath()
                        + "\n" + ex.getMessage());
                showData(btnShowText, null);
            }
            
            UIValidator v = new  UIValidator();
            Thread t = new Thread(v);
            t.start(); 
            
            
        }
    }

    private void search() {
        // Grab the text from the Search field
        String s = txtSearch.getText();
        GameElement g;
        // If it's a number, search by index, else search by name.
        try { 
            Integer i = Integer.parseInt(s);
            g = cave.searchById(i);
        }
        catch (NumberFormatException n) {
            g = cave.searchByName(s);
        }
        if (g == null) {
            textView.setText("Item not found.");
            showData(btnShowText, null);
        }
        else {
            showData(btnShowTree,(TreeNode) g);
        }
    }

    private void showItems() {
        // Clear the list of items and repopulate with the selected type
        cmbItems.removeAllItems();
        switch (cmbItemTypes.getSelectedIndex()) {
            case 0:
                cave.getParties().stream().forEach((p) -> {
                    cmbItems.addItem(p.getName()); });
                break;
            case 1:
                cave.getCreatures().stream().forEach((c) -> {
                    cmbItems.addItem(c.getName()); });
                break;
            case 2:
                cave.getTreasures().stream().forEach((t) -> {
                    cmbItems.addItem(t.getItemType() + ":" + t.getValue());});
                break;
            case 3:
                cave.getArtifacts().stream().forEach((a) -> {
                    cmbItems.addItem(a.getItemType() + ":" + a.getName()); });
                break;
        }
    }

    private void showSelectedItem(int index) {
        // Make sure list is valid to avoid nulls in case event is caused 
        // by the other handler clearing the list.
        if (!cmbItems.isValid()) return;
        // Depending on which item type is selected in the other list, pick the 
        // matching item by index from the corresponding collection.
        switch (cmbItemTypes.getSelectedIndex()) {
            case 0:
                showData(btnShowTree,cave.getParties().get(index));
                break;
            case 1:
                showData(btnShowTree,cave.getCreatures().get(index));                    
                break;
            case 2:
                showData(btnShowTree,cave.getTreasures().get(index));                    
                break;
            case 3:
                showData(btnShowTree,cave.getArtifacts().get(index));                    
                break;
        }            
    }

    enum SorterState {
        NEITHER, CREATURES, TREASURES;
    }
    
    class UIValidator implements Runnable {
        public void run() {
            while (true) {
                boolean go = false;
                for (Party p : cave.getParties()) if (p.needsRefresh) go = true;
                if (go) {
                    //for (Component c : jobView.getComponents()) jobView.remove(c);
                    //for (Party p : cave.getParties()) jobView.add(p.getDisplay());
                    validate();  
                }
                else try {Thread.sleep(100);} catch (InterruptedException ex) {}
            }        
        }
    }
}



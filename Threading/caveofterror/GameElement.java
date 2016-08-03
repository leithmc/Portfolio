package caveofterror;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * File: GameElement.java
 * Author: Leith McCombs
 * Date: Feb 4, 2016
 * Purpose: describe purpose of class here
 */
public abstract class GameElement implements Comparator<GameElement>{
    int id, owner, location;
    String name, type;
    GameElement(int id, String type, String name, int owner) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.owner = owner;
    }

    public String getItemType() {
        return type;
    }
    
    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compare(GameElement g1, GameElement g2) {
        return g1.id - g2.id;
    }
    
    public class Location {}
}

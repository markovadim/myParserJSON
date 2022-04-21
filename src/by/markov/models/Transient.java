package by.markov.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Transient {
    private int count;
    private String parent;
    private ArrayList<Integer> lst;
    private HashMap<Integer, String> map;

    public Transient(int count, String parent, ArrayList<Integer> lst) {
        this.count = count;
        this.parent = parent;
        this.lst = lst;
    }

    public Transient(int count, String parent, ArrayList<Integer> lst, HashMap<Integer, String> map) {
        this.count = count;
        this.parent = parent;
        this.lst = lst;
        this.map = map;
    }
}

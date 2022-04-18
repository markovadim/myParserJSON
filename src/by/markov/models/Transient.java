package by.markov.models;

import java.util.ArrayList;

public class Transient {
    private int count;
    private String parent;
    private ArrayList<Integer> lst;

    public Transient(int count, String parent, ArrayList<Integer> lst) {
        this.count = count;
        this.parent = parent;
        this.lst = lst;
    }
}

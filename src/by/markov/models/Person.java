package by.markov.models;

public class Person {
    private String name;
    private int age;
    private String email;
    private int[] notes;
    private double[] prices;
    private String[] clients;
    private Transient[] transients;

    private Transient aTransient;

    public Person(String name, int age, String email, int[] notes, double[] prices, String[] clients, Transient[] transients, Transient aTransient) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.notes = notes;
        this.prices = prices;
        this.clients = clients;
        this.transients = transients;
        this.aTransient = aTransient;
    }
}

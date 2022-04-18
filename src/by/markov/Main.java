package by.markov;

import by.markov.models.Person;
import by.markov.models.Transient;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {

        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(132);
        lst.add(25);
        lst.add(976);
        Transient tr = new Transient(23232, "Parent", lst);

        Person person = new Person("Vadim", 28, "markovadim@gmail.com", new int[]{5, 5, 5, 5}, new double[]{3.3, 4.4, 9.9}, new String[]{"Yulia", "Nikita"}, new Transient[]{null, null}, tr);

        MyParser<Person> parser = new MyParser<>();
        parser.saveObject(person);
    }
}

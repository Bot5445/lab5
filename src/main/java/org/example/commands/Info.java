package org.example.Commands;

import org.example.Data.Person;

import java.util.Map;
import java.util.TreeMap;

public class Show extends Command implements ICommand {

    private TreeMap<Integer, Person> coll;

    public Show(TreeMap<Integer, Person> coll) {
        this.coll = coll;
    }

    @Override
    public void execute() {
        System.out.println("-".repeat(10)+"Persons"+"-".repeat(10));
        for (Map.Entry<Integer, Person> x: coll.entrySet()) {
            System.out.printf("%-5s  |  %-5s%n", x.getKey(), x.getValue().getName());
        }
        System.out.println("-".repeat(27)+"\n");
    }

    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}

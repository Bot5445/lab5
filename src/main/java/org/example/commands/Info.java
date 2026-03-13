package org.example.commands;

import org.example.data.Person;

import java.util.Map;
import java.util.Map.Entry;


import static java.lang.String.format;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 */
public class Info implements ICommand {

    private final Map<Integer, Person> coll;

    public Info(Map<Integer, Person> coll) {
        this.coll = coll;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String execute() {
        StringBuilder str= new StringBuilder();

        str.append("-".repeat(10)+"Persons"+"-".repeat(10)+"\n");
        for (Entry<Integer, Person> x: coll.entrySet()) {
            str.append(format("%-5s  |  %-5s%n", x.getKey(), x.getValue().getName()));
        }
        str.append("-".repeat(27)).append("\n");
        return str.toString();
    }

    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}

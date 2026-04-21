package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;


import static java.lang.String.format;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 */

public class Info implements ICommand {

    private final ICollManager collectionManager;

    public Info(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "info";
    }

    /**
     * @return ID и имя person
     */
    @Override
    public String execute(String args) {
        if (collectionManager.isEmpty())
            return "Коллекция пустая";

        StringBuilder str= new StringBuilder();
        str.append("-".repeat(10)+"Persons"+"-".repeat(10)+"\n");
        for (Person p : collectionManager.getAllPersons()) {
            str.append(format("%-10s  |  %-10s%n", p.getId(), p.getName()));
        }
        str.append("-".repeat(27)+"\n");
        return str.toString();
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}

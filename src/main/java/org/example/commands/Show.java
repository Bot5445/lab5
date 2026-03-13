package org.example.commands;

import org.example.data.Person;

import java.util.Map;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 */
public class Show implements ICommand {
    /**
     * @return
     */
    @Override
    public String getName() {
        return "Show";
    }

    private final Map<Integer, Person> coll;

    public Show(Map<Integer, Person> coll) {
        this.coll = coll;
    }



    @Override
    public String execute() {
        StringBuilder str= new StringBuilder();
        for (Person person : coll.values()) {
            str.append(person.toString());            // Автоматически выведет: Person(id=1, name=Ivan, coordinates=..., ...)
        }
//        for (Person person : coll.values()) {
//            str += "%5s", person.get;
//        }
//        System.out.printf(str);
        return str.toString();
    }

    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

package org.example.Commands;

import org.example.Data.Person;

import java.util.Map;
import java.util.TreeMap;

public class Info extends Command implements ICommand {
    private TreeMap<Integer, Person> coll;

    public Info(TreeMap<Integer, Person> coll) {
        this.coll = coll;
    }

    @Override
    public void execute() {
        String str="";
        for (Person person : coll.values()) {
            System.out.println(person.toString());            // Автоматически выведет: Person(id=1, name=Ivan, coordinates=..., ...)
        }
//        for (Person person : coll.values()) {
//            str += "%5s", person.get;
//        }
//        System.out.printf(str);
    }

    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

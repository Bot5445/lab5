package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;


import static java.lang.String.format;

/**
 * Команда для вывода информации о коллекции.
 * Выводит список всех элементов в стандартный поток вывода (ID и Имя).
 */
public class Info implements ICommand {

    private final ICollManager collectionManager;

    /**
     * Создает команду информации о коллекции.
     * @param collectionManager менеджер коллекции
     */
    public Info(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     * @return строка "info"
     */
    @Override
    public String getName() {
        return "info";
    }

    /**
     * Выводит таблицу с ID и именами всех элементов коллекции.
     * Если коллекция пуста, возвращает соответствующее сообщение.
     * @param args аргументы (не используются)
     * @return строка с таблицей элементов или сообщение о пустоте
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
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    /**
     * Команда info не принимает аргументов
     * @return выводит false
     */
    @Override
    public boolean acceptsArguments() {
        return false;
    }
}

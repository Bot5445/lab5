package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;

/**
 * Выводит элементы, значение поля name которых начинается с заданной подстроки
 */
public class FilterStartsWithName implements ICommand {

    private final IGetterSetter collectionManager;

    public FilterStartsWithName(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "filter_starts_with_name";
    }

    /**
     * @param args префикс имени person
     * @return элементы, значение поля name которых начинается с заданной подстроки
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null || args.trim().isEmpty() || args.matches(".*\\d+.*")) {
            return "Ошибка: укажите префикс строку для поиска имени.";
        }
        String prefix = args.trim();
        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (Person p : collectionManager.getAllPersons()) {
            String name = p.getName();

            // name не может быть null по условию, но проверка не помешает
            if (name != null && name.startsWith(prefix)) {
                result.append(p.getId() + ": " + p + "\n");
                found = true;
            }
        }

        if (!found) {
            return "Элементы с именем, начинающимся на '" + prefix + "', не найдены.";
        }
        return result.toString();
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "выводит элементы, значение поля name которых начинается с заданной подстроки";
    }
}

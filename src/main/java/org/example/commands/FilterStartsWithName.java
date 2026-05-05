package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;

/**
 * Команда для фильтрации элементов коллекции по префиксу имени.
 * Выводит элементы, значение поля name которых начинается с заданной подстроки.
 */
public class FilterStartsWithName implements ICommand {

    private final IGetterSetter collectionManager;

    /**
     * Создает команду фильтрации по имени.
     * @param collectionManager менеджер коллекции для доступа к данным
     */
    public FilterStartsWithName(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     * @return строка "filter_starts_with_name"
     */
    @Override
    public String getName() {
        return "filter_starts_with_name";
    }

    /**
     * Выполняет поиск элементов, имя которых начинается с указанного префикса.
     * Префикс не должен содержать цифр.
     * @param args префикс строки для поиска в имени
     * @return строка с найденными элементами или сообщение об ошибке/отсутствии результатов
     * @throws Exception при ошибке доступа к коллекции
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
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит элементы, значение поля name которых начинается с заданной подстроки";
    }
}

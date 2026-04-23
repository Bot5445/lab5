package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFactory;

import java.util.Arrays;

/**
 * Удаляет из коллекции все элементы, меньшие, чем заданный
 */
public class RemoveLower implements ICommand{
    private final ICollManager collectionManager;

    public RemoveLower(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "remove_lower";
    }

    /**
     * @param args данные для сравнения
     * @return сколько удалено объектов
     * @throws Exception ошибка сравнения
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null) {
            return "Ошибка: не переданы данные элемента для сравнения.";
        }

        try {
            // 1. Создаем шаблон объекта из введенных данных
            String[] fullValues = args.split(",", -1);
            int expectedLength = Person.getHeaders().length;
            if (fullValues.length < expectedLength) {
                fullValues = Arrays.copyOf(fullValues, expectedLength);
            }

            Person template = PersonFactory.createFromStringArray(fullValues);

            // 2. Удаляем элементы меньше шаблона
            // (CollectionManager сравнивает по ID, если там нет другой логики)
            int removedCount = collectionManager.removeLower(template);

            return "Удалено элементов: " + removedCount;
        } catch (Exception e) {
            return "Ошибка при создании элемента сравнения: " + e.getMessage();
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "удаляет из коллекции все элементы, меньшие, чем заданный";
    }

    @Override
    public boolean requiresCompoundDataInput() {
        return true;
    }
}

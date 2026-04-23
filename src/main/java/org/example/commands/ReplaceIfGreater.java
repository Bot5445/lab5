package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFactory;
import org.example.data.PersonFieldUpdater;

import java.util.Arrays;

/**
 * Заменяет значение по ключу, если новое значение больше старого
 */
public class ReplaceIfGreater implements ICommand{
    private final ICollManager collectionManager;

    public ReplaceIfGreater(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "replace_if_greater";
    }

    /**
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null || args.isEmpty()) {
            return "Ошибка: данные не были предоставлены. Сначала укажите ID.";
        }

        try {
            // 1. Разбираем данные (ID + остальные поля)
            String[] fullValues = args.split(",", -1);
            int expectedLength = Person.getHeaders().length;
            if (fullValues.length < expectedLength) {
                fullValues = Arrays.copyOf(fullValues, expectedLength);
            }

            // 2. Извлекаем ID (он идет первым)
            int id = Integer.parseInt(fullValues[0]);

            if (!collectionManager.containsId(id)) {
                return "Элемент с ID " + id + " не найден.";
            }

            // 3. Создаем объекты для сравнения
            Person newPerson = PersonFactory.createFromStringArray(fullValues);
            Person oldPerson = collectionManager.getPersonById(id);

            // 4. Сравниваем (например, по росту)
            // Если рост нового больше роста старого -> заменяем
            if (newPerson.getHeight() > oldPerson.getHeight()) {
                collectionManager.addPerson(newPerson);
                return "Значение заменено. Новый рост (" + newPerson.getHeight() + ") больше старого (" + oldPerson.getHeight() + ").";
            } else {
                return "Замена не требуется. Новый рост (" + newPerson.getHeight() + ") не больше старого (" + oldPerson.getHeight() + ").";
            }

        } catch (NumberFormatException e) {
            return "Ошибка формата числа: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка при обновлении: " + e.getMessage();
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "заменяет значение по ключу, если новое значение больше старого";
    }

    @Override
    public boolean requiresCompoundDataInput() {
        return true;
    }
}

package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFieldUpdater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Удалить из коллекции все элементы, ключ которых меньше, чем заданный
 */
public class RemoveLowerKey implements ICommand{
    private final ICollManager collectionManager;

    public RemoveLowerKey(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return remove_lower_key
     */
    @Override
    public String getName() {
        return "remove_lower_key";
    }

    /**
     * @param args аргументы
     * @return количество удаленных Person
     * @throws Exception если не число
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null) {
            return "Ошибка: укажите поле и значение. Пример: height 170";
        }

        String[] parts = args.trim().split("\\s+", 2);
        if (parts.length < 2) {
            return "Ошибка: недостаточно аргументов. Пример: height 170";
        }

        String fieldName = parts[0];
        String thresholdStr = parts[1];
        try {
            double threshold = Double.parseDouble(thresholdStr);
            int removedCount = 0;

            // Используем итератор для безопасного удаления
            Iterator<Person> iterator = collectionManager.getAllPersons().iterator();
            while (iterator.hasNext()) {
                Person p = iterator.next();
                try {
                    String valStr = PersonFieldUpdater.getFieldValue(p, fieldName);
                    double val = Double.parseDouble(valStr);

                    if (val < threshold) {
                        // Внимание: удаляем через менеджер, но итератор может стать недействительным,
                        // если коллекция не поддерживает удаление во время итерации.
                        // Лучше собрать ID для удаления.
                        // Но для простоты используем простой цикл или removeLowerKey менеджера, если там ID.
                        // Здесь реализуем через сбор ID.
                        // (Для простоты кода, если CollectionManager позволяет удалять по ID)
                        int idToRemove = p.getId();
                        // Нельзя удалять внутри итератора values(), если это не iterator.remove()
                        // Но у нас есть только ICollManager.
                        // Соберем ID в список.
                    }
                } catch (NumberFormatException e) {
                    // Если поле не число, пропускаем
                } catch (IllegalArgumentException e) {
                    return "Ошибка: " + e.getMessage();
                }
            }

            // Перезаписываем логику с удалением по ID
            // Чтобы не ломать итератор, пробежимся еще раз или изменим логику.
            // Самый простой способ - удалить через Stream или цикл for (но с копией коллекции или ID)

            // Простая реализация через удаление по ID:
            // Получаем все ID, где поле меньше порога
            List<Integer> idsToRemove = new ArrayList<>();
            for (Person p : collectionManager.getAllPersons()) {
                try {
                    String valStr = PersonFieldUpdater.getFieldValue(p, fieldName);
                    double val = Double.parseDouble(valStr);
                    if (val < threshold) {
                        idsToRemove.add(p.getId());
                    }
                } catch (Exception ignored) {}
            }
            for (Integer id : idsToRemove) {
                collectionManager.deletePerson(id);
                removedCount++;
            }

            return "Удалено элементов с " + fieldName + " меньше " + threshold + ": " + removedCount;

        } catch (NumberFormatException e) {
            return "Ошибка: значение для сравнения должно быть числом.";
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "удаляет из коллекции все элементы, ключ которых меньше, чем заданный";
    }
}

package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFactory;

import java.util.Arrays;

/**
 * Добавляет новый элемент с заданным ключом
 */
public class Insert implements ICommand{
    private final ICollManager collectionManager;

    public Insert(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "insert";
    }

    /**
     * @param args
     * @return
     */
    @Override
    public String execute(String args) {

//            String[] fullValues = args.split(",", -1);

//            String idStr = fullValues[0];
//            if (idStr != null && !idStr.isBlank() && !idStr.equalsIgnoreCase("null")) {
//                try {
//                    long id = Long.parseLong(idStr);
//                    if (collectionManager.containsKey(id)) {
//                        return "Ошибка: Элемент с ID " + id + " уже существует.";
//                    }
//                } catch (NumberFormatException e) {
//                    return "Ошибка: ID должен быть числом.";
//                }
//            }

        if (args == null || args.isEmpty()) {
            return "Ошибка: данные не были предоставлены.";
        }

        try {
            // 1. Разбираем CSV строку от PersonInputReader
            String[] fullValues = args.split(",", -1);

            // 2. Получаем эталонный размер массива напрямую из класса Person
            int expectedLength = Person.getHeaders().length;

            // 3. Если пользователь ввел меньше данных, чем нужно, дополняем null
            if (fullValues.length < expectedLength) {
                // Arrays.copyOf автоматически дополнит массив null-ами, если новый размер больше текущего
                fullValues = Arrays.copyOf(fullValues, expectedLength);
            }

            // Если пользователь ввел БОЛЬШЕ данных (мусор в конце), можно обрезать
            if (fullValues.length > expectedLength) {
                fullValues = Arrays.copyOfRange(fullValues, 0, expectedLength);
            }
//            // Дополняем массив null-ами до нужной длины (чтобы пользователь мог не вводить поля в конце)
//            String[] headers = Person.getHeaders();
//            if (fullValues.length < headers.length) {
//                fullValues = Arrays.copyOf(fullValues, headers.length);
//            }

            // Создание объекта через фабрику
            Person newPerson = PersonFactory.createFromStringArray(fullValues);
            collectionManager.addPerson(newPerson);

            return "Элемент успешно добавлен с ключом " + newPerson.getId() + ".";

        } catch (NumberFormatException e) {
            return "Ошибка формата числа: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            // Ошибки валидации (имя, ID <= 0 и т.д.)
            return "Ошибка данных: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка при создании объекта: " + e.getMessage();
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "добавляет нового Person. Если первый аргумент число - это ID, иначе ID генерируется. " +
                "Формат полей: name рост [паспортID]";
//                "Формат полей: " + Arrays.toString(Person.getHeaders());
    }

    /**
     * Чтобы начать опрос пользователя
     * @return true
     */
    @Override
    public boolean requiresCompoundDataInput() { return true; }
}

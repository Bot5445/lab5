package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFactory;

import java.util.Arrays;
import java.util.Map;

/**
 * Добавляет новый элемент {@link Person} в коллекцию.
 */
public class Insert implements ICommand{
    private final ICollManager collectionManager;

    /**
     * Создает команду вставки.
     * @param collectionManager менеджер коллекции
     */
    public Insert(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает имя команды.
     * @return строка "insert"
     */
    @Override
    public String getName() {
        return "insert";
    }

    /**
     * Создает и добавляет объект Person на основе полученных данных.
     * Если данные неполные, недостающие поля заполняются значениями по умолчанию (null).
     * @param args CSV-строка с данными объекта (формат: id,name,coordX,coordY,height...)
     * @return сообщение об успешном добавлении с указанием ID или текст ошибки валидации
     */
    @Override
    public String execute(String args) {
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
     * Возвращает описание команды для справки.
     * @return текстовое описание
     */
    @Override
    public String getDescription() {
        return "добавляет нового Person. Если первый аргумент число - это ID, иначе ID генерируется. " +
                "Формат полей: name рост [паспортID]";
//                "Формат полей: " + Arrays.toString(Person.getHeaders());
    }

    /**
     * Указывает, что для выполнения команды требуется интерактивный ввод данных объекта.
     * @return true, так как команда требует ввода составного объекта
     */
    @Override
    public boolean requiresCompoundDataInput() { return true; }
}

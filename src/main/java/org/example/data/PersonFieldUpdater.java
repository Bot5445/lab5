package org.example.data;

import java.text.ParseException;

/**
 *
 */
public class PersonFieldUpdater {

    /**
     * Обновляет поле объекта Person.
     * @param oldPerson старый объект
     * @param fieldName имя поля (например, "name")
     * @param newValue новое значение в виде строки
     * @return новый объект Person с обновленным полем
     */
    public static Person updateField(Person oldPerson, String fieldName, String newValue) throws ParseException {
        // Превращаем человека в массив строк (как это было в командах)
        String[] values = oldPerson.toString().split(",");
        String[] headers = Person.getHeaders(); // Получаем заголовки ["id", "name", ...]

        // Ищем индекс поля (та самая логика цикла)
        int indexToUpdate = -1;
        for (int i = 0; i < headers.length; i++) {
            // Сравниваем имя поля с заголовком (убираем префиксы, если есть, например "coordinates: x")
            String headerName = headers[i].contains(":") ? headers[i].split(":")[1].trim() : headers[i].trim();

            if (headerName.equalsIgnoreCase(fieldName)) {
                indexToUpdate = i;
                break;
            }
        }

        if (indexToUpdate == -1) {
            throw new IllegalArgumentException("Поле '" + fieldName + "' не найдено.");
        }
        if (headers[indexToUpdate].equalsIgnoreCase("id")) {
            throw new IllegalArgumentException("Изменение ID запрещено.");
        }

        // Меняем значение
        values[indexToUpdate] = newValue;

        // Собираем объект заново через Фабрику (это решает проблему SRP)
        return PersonFactory.createFromStringArray(values);
    }

    /**
     * Получает текущее значение поля в виде строки (нужно для ReplaceIfGreater)
     */
    public static String getFieldValue(Person person, String fieldName) {
        // Аналогичная логика поиска индекса, но возврат значения
        String[] values = person.toString().split(",");
        String[] headers = Person.getHeaders();

        for (int i = 0; i < headers.length; i++) {
            String headerName = headers[i].contains(":") ? headers[i].split(":")[1].trim() : headers[i].trim();
            if (headerName.equalsIgnoreCase(fieldName)) {
                return values[i];
            }
        }
        throw new IllegalArgumentException("Поле не найдено");
    }
}

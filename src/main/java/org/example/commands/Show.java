package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Команда для вывода всех элементов коллекции в виде форматированной таблицы.
 * Если коллекция пуста, выводит соответствующее сообщение.
 */
public class Show implements ICommand {
    private final ICollManager collectionManager;

    /**
     * Создает команду вывода содержимого коллекции.
     *
     * @param collectionManager менеджер, предоставляющий доступ к коллекции
     */
    public Show(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     *
     * @return строка "show"
     */
    @Override
    public String getName() {
        return "show";
    }

    /**
     * Выполняет формирование и вывод таблицы элементов.
     * Метод определяет ширину колонок динамически на основе заголовков и данных,
     * ограничивая максимальную ширину для удобства чтения.
     *
     * @param args аргументы команды (не используются в данной реализации)
     * @return строка с отформатированной таблицей или сообщение о том, что коллекция пуста
     */
    @Override
    public String execute(String args) {
        if (collectionManager.isEmpty()) return "Коллекция пустая";

        StringJoiner str = new StringJoiner("\n");

        String[] headers = {"ID", "Name", "Coords", "Date", "Height", "Passport", "Color", "Country", "Location"};
        int[] colWidth = {20, 12, 15, 10, 6, 12, 8, 10, 18};

        StringBuilder rowFormatBuilder = new StringBuilder("|");
        for (int w : colWidth) {
            rowFormatBuilder.append(" %-" + w + "s |");
        }
        String rowFormat = rowFormatBuilder.toString();

        // Шапка
        str.add(String.format(rowFormat, (Object[]) headers));

        // Разделитель
        String[] dashes = new String[headers.length];
        for (int i = 0; i < headers.length; i++) dashes[i] = "-".repeat(colWidth[i]);
        str.add(String.format(rowFormat, (Object[]) dashes));

        // Данные
        for (Person person : collectionManager.getAllPersons()) {
            String[] raw = person.toString().split(",", -1);
            if (raw.length < 12) raw = Arrays.copyOf(raw, 12);
            for (int i = 0; i < 12; i++) {
                raw[i] = (raw[i] == null) ? "" : raw[i].trim();
            }

            // Формируем сжатые строки для объединенных колонок
            String[] displayRow = new String[headers.length];
            displayRow[0] = prettify(raw[0], colWidth[0]);                     // ID
            displayRow[1] = prettify(raw[1], colWidth[1]);                     // Name
            displayRow[2] = prettify(raw[2] + ", " + raw[3], colWidth[2]);     // Coords (x, y)
            displayRow[3] = prettify(raw[4], colWidth[3]);                     // Date
            displayRow[4] = prettify(raw[5], colWidth[4]);                     // Height
            displayRow[5] = prettify(raw[6], colWidth[5]);                     // Passport
            displayRow[6] = prettify(raw[7], colWidth[6]);                     // Color
            displayRow[7] = prettify(raw[8], colWidth[7]);                     // Country
            displayRow[8] = prettify(raw[9] + ", " + raw[10] + ", " + raw[11], colWidth[8]); // Location (x, y, name)

            str.add(String.format(rowFormat, (Object[]) displayRow));
        }
        return str.toString();
    }

    /**
     * Обрезает длинные значения (добавляя …) и заменяет "null" на "-"
     */
    private String prettify(String s, int maxLen) {
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("null")) {
            return "-"; // Заменяем null на дефис, чтобы таблица была компактнее
        }
        s = s.trim();
        if (s.length() > maxLen) {
            return s.substring(0, maxLen - 1) + "…"; // Обрезка с многоточием
        }
        return s;
    }

    /**
     * Обрезает строку до указанной длины, добавляя многоточие «…» на конце, если строка длиннее.
     *
     * @param s         исходная строка
     * @param maxLength максимальная допустимая длина строки
     * @return обрезанная строка или исходная, если её длина в пределах нормы
     */
    private String truncate(String s, int maxLength) {
//        if (s == null) return "";
//        if (s.length() <= maxLength) return s;
//        return s.substring(0, maxLength - 1) + "…";
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("null")) {
            return "-"; // Заменяем null на дефис, чтобы таблица была компактнее
        }
        s = s.trim();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength - 1) + "…"; // Обрезка с многоточием
        }
        return s;
    }

    /**
     * Возвращает описание команды для справки.
     *
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит все элементы коллекции в строковом представлении";
    }

    /**
     * Команда show не принимает аргументов
     * @return выводит false
     */
    @Override
    public boolean acceptsArguments() {
        return false;
    }
}
package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;

import java.util.ArrayList;
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
     * @param collectionManager менеджер, предоставляющий доступ к коллекции
     */
    public Show(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
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
     * @param args аргументы команды (не используются в данной реализации)
     * @return строка с отформатированной таблицей или сообщение о том, что коллекция пуста
     */
    @Override
    public String execute(String args) {
        if (collectionManager.isEmpty()) return "Коллекция пустая";

        StringJoiner str = new StringJoiner("\n");
        String[] headers = Person.getHeaders();
        int numCols = headers.length;
        int maxColWidth = 22; // Максимальная ширина колонки

        // Собираем все строки данных
        List<String[]> rows = new ArrayList<>();
        for (Person person : collectionManager.getAllPersons()) {
            String[] rowData = person.toString().split(",", -1);
            if (rowData.length < numCols) {
                rowData = java.util.Arrays.copyOf(rowData, numCols);
            }
            rows.add(rowData);
        }

        // Вычисляем ширину каждой колонки по реальным данным
        int[] colWidth = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            colWidth[i] = headers[i].length();
            for (String[] row : rows) {
                if (i < row.length) {
                    colWidth[i] = Math.max(colWidth[i], row[i].trim().length());
                }
            }
            // Ограничиваем максимальную ширину
            colWidth[i] = Math.min(colWidth[i], maxColWidth);
        }

        // Строим форматную строку: | %-12s | %-15s | ...
        StringBuilder fmtBuilder = new StringBuilder("|");
        for (int w : colWidth) {
            fmtBuilder.append(" %-").append(w).append("s |");
        }
        String rowFormat = fmtBuilder.toString();

        // Функция обрезки длинных значений
        // (лямбда для финальной переменной)
        int maxW = maxColWidth;

        // Шапка
        String[] displayHeaders = new String[numCols];
        for (int i = 0; i < numCols; i++) {
            displayHeaders[i] = truncate(headers[i], maxW);
        }
        str.add(String.format(rowFormat, (Object[]) displayHeaders));

        // Разделитель
        String[] dashes = new String[numCols];
        for (int i = 0; i < numCols; i++) {
            dashes[i] = "-".repeat(colWidth[i]);
        }
        str.add(String.format(rowFormat, (Object[]) dashes));

        // Строки данных
        for (String[] row : rows) {
            String[] displayRow = new String[numCols];
            for (int i = 0; i < numCols; i++) {
                String val = (i < row.length) ? row[i].trim() : "";
                displayRow[i] = truncate(val, maxW);
            }
            str.add(String.format(rowFormat, (Object[]) displayRow));
        }

        return str.toString();
    }

    /**
     * Обрезает строку до указанной длины, добавляя многоточие «…» на конце, если строка длиннее.
     * @param s исходная строка
     * @param maxLength максимальная допустимая длина строки
     * @return обрезанная строка или исходная, если её длина в пределах нормы
     */
    private String truncate(String s, int maxLength) {
        if (s == null) return "";
        if (s.length() <= maxLength) return s;
        return s.substring(0, maxLength - 1) + "…";
    }

    /**
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит все элементы коллекции в строковом представлении";
    }
}
package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;

import java.util.StringJoiner;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 */
public class Show implements ICommand {
    private final ICollManager collectionManager;

    public Show(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String execute(String args) {
        if (collectionManager.isEmpty()) return "Коллекция пустая";

        StringJoiner str= new StringJoiner("\n");

        // 2. Определяем формат строки (как в printf)
        String[] headerPerson=Person.getHeaders();
       int[] colLength = {12,15,15, 15, 30, 10, 10,10,15,15,15,15};         // | %-5s | %-15s | %10s |
        String rowFormat="|";
        for (int i:colLength) {
            rowFormat+=" %-"+i+"s |";
        }
//        String rowFormat ="| %-"+colLength[0]+"s | %-"+colLength[1]+"s |"+("%-"+colLength[2]+"s |").repeat(headerPerson.length-2);

        // 3. Создаем шапку таблицы
        String header = String.format(rowFormat, (Object[]) headerPerson);
        str.add(header);

        // Строим массив строк-разделителей, соответствующий количеству колонок
        String[] dashes = new String[headerPerson.length];
        for (int i = 0; i < headerPerson.length; i++) {
            dashes[i] = "-".repeat(colLength[i]);      // для ширины 10
        }
        String separator = String.format(rowFormat, (Object[]) dashes);
        str.add(separator);

        for (Person person : collectionManager.getAllPersons()) {
            String[] rowData= person.toString().split(",");
            str.add(String.format(rowFormat, (Object[]) rowData));// Автоматически выведет: Person(id=1, name=Ivan, coordinates=..., ...)
        }
        return  str.toString();

    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

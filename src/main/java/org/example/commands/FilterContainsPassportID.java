package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;

/**
 * Команда для фильтрации элементов коллекции по подстроке в поле passportID.
 * Выводит элементы, значение поля passportID которых содержит заданную подстроку.
 */
public class FilterContainsPassportID implements ICommand {
    private final IGetterSetter collectionManager;

    /**
     * Создает команду фильтрации по passportID.
     * @param collectionManager менеджер коллекции для доступа к данным
     */
    public FilterContainsPassportID(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     * @return строка "filter_contains_passport_i_d"
     */
    @Override
    public String getName() {
        return "filter_contains_passport_i_d";
    }

    /**
     * Выполняет поиск элементов, passportID которых содержит указанную подстроку.
     * Аргумент должен состоять только из цифр (пробелы игнорируются).
     * @param args подстрока для поиска в passportID
     * @return строка с найденными элементами или сообщение об ошибке/отсутствии результатов
     * @throws Exception при ошибке доступа к коллекции
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null || args.trim().isEmpty() || !args.replace(" ", "").matches("\\d+")) {
            return "Ошибка: укажите строку, состоящая из чисел, для поиска в passportID.";
        }
        String substring = args.trim().replace(" ", "");

        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (Person p : collectionManager.getAllPersons()) {
            String passport = p.getPassportID();

            // Проверяем, что passportID не null и содержит подстроку
            if (passport != null && passport.contains(substring)) {
                result.append(p.getId() + ": " + p + "\n");
                found = true;
            }
        }
        if (!found) {
            return "Элементы с passportID, содержащим '" + substring + "', не найдены.";
        }

        return result.toString();
    }

    /**
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит элементы, значение поля passportID которых содержит заданную подстроку.";
    }
}

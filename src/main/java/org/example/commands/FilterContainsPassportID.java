package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;

/**
 * Выводит элементы, значение поля passportID которых содержит заданную подстроку
 */
public class FilterContainsPassportID implements ICommand {
    private final IGetterSetter collectionManager;

    public FilterContainsPassportID(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "filter_contains_passport_i_d";
    }

    /**
     * @param args аргумент по которому происходит фильтрация
     * @return элементы, значение поля passportID которых содержит заданную подстроку
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
     * @return описание
     */
    @Override
    public String getDescription() {
        return "выводит элементы, значение поля passportID которых содержит заданную подстроку.";
    }
}

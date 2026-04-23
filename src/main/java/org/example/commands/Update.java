package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFieldUpdater;

/**
* Обновляет значение элемента коллекции, id которого равен заданному
* */
public class Update implements ICommand{
    private final ICollManager collectionManager;

    public Update(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "update";
    }

    /**
     * @param args данные person
     * @return обнавлены ли поля
     * @throws Exception неверный ID
     */
    @Override
    public String execute(String args) throws Exception {
        String[] arg = args.split(" ", 3); // ID, field, value
        if (args == null || arg.length < 3) {
            return "Ошибка ввода: необходимо указать ID, поле и значение.";
        }

        try {
            int id = Integer.parseInt(arg[0]);
            String fieldName = arg[1];
            String newValue = arg[2];

            if (!collectionManager.containsId(id)) return "Элемент с ID " + id + " не найден.";

            Person oldPerson = collectionManager.getPersonById(id);

            Person updatedPerson = PersonFieldUpdater.updateField(oldPerson, fieldName, newValue);

            collectionManager.addPerson(updatedPerson);
            return "Поле '" + fieldName + "' для объекта " + id + " успешно обновлено.";

        } catch (NumberFormatException e) {
            return "Ошибка: ID должен быть числом.";
        } catch (Exception e) {
            return "Ошибка при обновлении: " + e.getMessage();
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "обновить значение поля элемента по ID";
    }

    /**
     * @return
     */
    @Override
    public boolean requiresCompoundDataInput() {
        return true;
    }
}

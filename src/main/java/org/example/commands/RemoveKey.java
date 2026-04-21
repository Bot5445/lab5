package org.example.commands;

import org.example.data.ICollManager;

/**
 *Удаляет элемент из коллекции по его ключу
 */
public class RemoveKey implements ICommand {

    private final ICollManager collectionManager;

    public RemoveKey(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "remove_key";
    }

    /**
     * @param args ID
     * @return удалился ли person
     */
    @Override
    public String execute(String args) {
        if (args == null) {
            return "Ошибка: укажите ключ (ID) для удаления.";
        }
        try {
            int id = Integer.parseInt(args.trim());

            if (!collectionManager.containsId(id)) {
                return "Элемент с ID " + id + " не найден.";
            }

            collectionManager.deletePerson(id);
            return "Элемент с ID " + id + " успешно удален.";

        } catch (NumberFormatException e) {
            return "Ошибка: ID должен быть целым числом.";
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "удаляет элемент из коллекции по его ключу";
    }
}

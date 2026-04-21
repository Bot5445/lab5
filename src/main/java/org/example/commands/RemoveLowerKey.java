package org.example.commands;

import org.example.data.ICollManager;

/**
 * Удалить из коллекции все элементы, ключ которых меньше, чем заданный
 */
public class RemoveLowerKey implements ICommand{
    private final ICollManager collectionManager;

    public RemoveLowerKey(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return remove_lower_key
     */
    @Override
    public String getName() {
        return "remove_lower_key";
    }

    /**
     * @param args аргументы
     * @return количество удаленных Person
     * @throws Exception если не число
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null) {
            return "Ошибка: укажите ключ.";
        }

        try {
            int id = Integer.parseInt(args.trim());
            int removedCount = collectionManager.removeLowerKey(id);

            return "Удалено элементов с ключом меньше " + id + ": " + removedCount;

        } catch (NumberFormatException e) {
            return "Ошибка: ключ должен быть числом.";
        }
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "удаляет из коллекции все элементы, ключ которых меньше, чем заданный";
    }
}

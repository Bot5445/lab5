package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFieldUpdater;

/**
 * Заменяет значение по ключу, если новое значение больше старого
 */
public class ReplaceIfGreater implements ICommand{
    private final ICollManager collectionManager;

    public ReplaceIfGreater(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "replace_if_greater";
    }

    /**
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public String execute(String args) throws Exception {
        String[] arg= args.split(" ", 3); // Делим на 3 части: id, field, value
        if (args == null || arg.length == 2) {
            return "Ошибка ввода: необходимо указать ID и параметр=значение. Использование: \"update id param value\"";
        }

        try {

            int id = Integer.parseInt(arg[0]);
            String fieldName = arg[1]; // название переменной
            String newValue = arg[2]; // значение переменной

            if (!collectionManager.containsId(id)) return "Элемент с ID " + id + " не найден.";


            // Получаем старый объект и превращаем его в массив строк
            Person oldPerson = collectionManager.getPersonById(id);

            // Получаем старое значение
            String oldValueStr = PersonFieldUpdater.getFieldValue(oldPerson, fieldName);

            // Сравниваем (предполагаем, что числа)
            double oldVal = Double.parseDouble(oldValueStr);
            double newVal = Double.parseDouble(newValue);

            if (newVal > oldVal) {
                Person updatedPerson = PersonFieldUpdater.updateField(oldPerson, fieldName, newValue);
                collectionManager.addPerson(updatedPerson);
                return "Значение заменено.";
            } else {
                return "Новое значение не больше старого.";
            }

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
        return "заменяет значение по ключу, если новое значение больше старого";
    }
}

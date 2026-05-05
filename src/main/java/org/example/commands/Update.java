package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFieldUpdater;

/**
 * Команда для обновления значения конкретного поля элемента коллекции по его ID.
 */
public class Update implements ICommand{
    private final ICollManager collectionManager;

    /**
     * Создает команду обновления элемента.
     * @param collectionManager менеджер коллекции, предоставляющий доступ к данным
     */
    public Update(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     * @return строка "update"
     */
    @Override
    public String getName() {
        return "update";
    }

    /**
     * Выполняет обновление поля объекта.
     * Ожидает аргументы в формате: "ID имя_поля новое_значение".
     * Если элемент с указанным ID существует, создает его копию с обновленным полем.
     * @param args строка с аргументами, разделенными пробелами (ID, имя поля, значение)
     * @return сообщение об успешном обновлении или текст ошибки
     * @throws Exception если произошла ошибка при поиске элемента или обновлении поля
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
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "обновить значение поля элемента по ID";
    }

    /**
     * Указывает, что команда требует ввода сложных данных.
     * Возвращает true, что сигнализирует о необходимости интерактивного ввода
     * или специальной обработки аргументов.
     * @return true
     */
    @Override
    public boolean requiresCompoundDataInput() {
        return true;
    }
}

package org.example.commands;

import org.example.data.ICollManager;
import org.example.data.Person;
import org.example.data.PersonFactory;
import org.example.data.PersonFieldUpdater;

import java.util.Arrays;

/**
 * Команда для замены значения элемента в коллекции по ключу (ID),
 * если новое значение больше старого.
 * Сравнение объектов происходит по полю {@code height} (рост).
 * Если рост нового объекта больше роста существующего объекта с таким же ID,
 * то происходит замена.
 */
public class ReplaceIfGreater implements ICommand{
    private final ICollManager collectionManager;

    /**
     * Конструктор команды.
     *
     * @param collectionManager менеджер коллекции, предоставляющий доступ к данным
     */
    public ReplaceIfGreater(ICollManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     * @return строка "replace_if_greater"
     */
    @Override
    public String getName() {
        return "replace_if_greater";
    }

    /**
     * Выполняет замену элемента, если новое значение больше старого.
     * Логика выполнения:
     *   1. Разбирает строку аргументов (CSV формат), извлекая ID и данные нового объекта.
     *   2. Проверяет наличие элемента с указанным ID в коллекции.
     *   3. Создает объект {@link Person} из переданных данных.
     *   4. Сравнивает поле height нового и старого объектов.
     *   5. Если новый рост строго больше старого, заменяет элемент в коллекции.
     * @param args строка с данными в формате CSV, где первый элемент — ID,
     *             а остальные — поля объекта Person.
     * @return сообщение о результате выполнения (замена произведена, замена не требуется или ошибка).
     * @throws Exception если произошла ошибка при парсинге данных или создании объекта.
     */
    @Override
    public String execute(String args) throws Exception {
        if (args == null || args.isEmpty()) {
            return "Ошибка: данные не были предоставлены. Сначала укажите ID.";
        }

        try {
            // 1. Разбираем данные (ID + остальные поля)
            String[] fullValues = args.split(",", -1);
            int expectedLength = Person.getHeaders().length;
            if (fullValues.length < expectedLength) {
                fullValues = Arrays.copyOf(fullValues, expectedLength);
            }

            // 2. Извлекаем ID (он идет первым)
            int id = Integer.parseInt(fullValues[0]);

            if (!collectionManager.containsId(id)) {
                return "Элемент с ID " + id + " не найден.";
            }

            // 3. Создаем объекты для сравнения
            Person newPerson = PersonFactory.createFromStringArray(fullValues);
            Person oldPerson = collectionManager.getPersonById(id);

            // 4. Сравниваем (например, по росту)
            // Если рост нового больше роста старого -> заменяем
            if (newPerson.getHeight() > oldPerson.getHeight()) {
                collectionManager.addPerson(newPerson);
                return "Значение заменено. Новый рост (" + newPerson.getHeight() + ") больше старого (" + oldPerson.getHeight() + ").";
            } else {
                return "Замена не требуется. Новый рост (" + newPerson.getHeight() + ") не больше старого (" + oldPerson.getHeight() + ").";
            }

        } catch (NumberFormatException e) {
            return "Ошибка формата числа: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка при обновлении: " + e.getMessage();
        }
    }

    /**
     * Возвращает описание команды для справки.
     * @return описание функционала команды
     */
    @Override
    public String getDescription() {
        return "заменяет значение по ключу, если новое значение больше старого";
    }

    /**
     * Указывает, что команда требует ввода сложного объекта (Person).
     * Это сигнал для {@link org.example.CommandExecutor} запустить интерактивный опрос полей.
     * @return {@code true}, так как команда требует ввода данных объекта.
     */
    @Override
    public boolean requiresCompoundDataInput() {
        return true;
    }
}

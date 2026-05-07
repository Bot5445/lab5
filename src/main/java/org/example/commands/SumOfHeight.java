package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;

/**
 * Команда для вывода суммы значений поля height для всех элементов коллекции.
 */
public class SumOfHeight implements ICommand{
    private final IGetterSetter collectionManager;

    /**
     * Создает команду подсчета суммы роста.
     * @param collectionManager менеджер коллекции для доступа к данным
     */
    public SumOfHeight(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает название команды.
     * @return строка "sum_of_height"
     */
    @Override
    public String getName() {
        return "sum_of_height";
    }

    /**
     * Вычисляет и возвращает сумму значений поля height всех объектов в коллекции.
     * @param args аргументы команды (не используются)
     * @return строковое представление суммы роста всех элементов
     * @throws Exception в случае ошибки доступа к данным
     */
    @Override
    public String execute(String args) throws Exception {
        int sum = 0;
        for (Person person: this.collectionManager.getAllPersons()) {
            sum += person.getHeight();
        }
        return String.valueOf(sum);
    }

    /**
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит сумму значений поля height для всех элементов коллекции";
    }

    /**
     * Команда sum_of_height не принимает аргументов
     * @return выводит false
     */
    @Override
    public boolean acceptsArguments() {
        return false;
    }
}

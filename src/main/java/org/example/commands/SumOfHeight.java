package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;

/**
 * Выводит сумму значений поля height для всех элементов коллекции
 */
public class SumOfHeight implements ICommand{
    private final IGetterSetter collectionManager;

    public SumOfHeight(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "sum_of_height";
    }

    /**
     * @return сумму значений поля height для всех элементов коллекции
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
     * @return описание
     */
    @Override
    public String getDescription() {
        return "выводит сумму значений поля height для всех элементов коллекции";
    }
}

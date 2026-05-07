package org.example.commands;

import org.example.data.IGetterSetter;

/**
 * Очищает коллекцию
 */
public class Clear implements ICommand {
    private final IGetterSetter collectionManager;

    /**
     * Создает команду очистки коллекции.
     * @param collectionManager менеджер коллекции, который будет очищен
     */
    public Clear(IGetterSetter collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "clear";
    }

    /**
     * @param args аргументы
     * @return отчищена ли коллекция
     */
    @Override
    public String execute(String args) {
        collectionManager.getPerson().clear();
        return "Коллекция отчищена";
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "очищает коллекцию";
    }

    /**
     * Команда clear не принимает аргументов
     * @return выводит false
     */
    @Override
    public boolean acceptsArguments() {
        return false;
    }
}

package org.example.commands;

public interface ICommand {
    String getName();

    String execute(String args) throws Exception;

    String getDescription();

    /**
     * Ожидает ли команда составной объект (Person)
     * в формате CSV, а не примитивные аргументы в одной строке.
     * Клиент использует этот флаг для запуска интерактивного ввода.
     */
    default boolean requiresCompoundDataInput() {
        return false;
    }

}

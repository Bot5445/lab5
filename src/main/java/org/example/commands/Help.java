package org.example.commands;

import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

/**
 * Команда для вывода справки по всем доступным командам приложения.
 * Динамически формирует список на основе переданного реестра команд.
 */
public class Help implements ICommand {
    //    private CommandExecutor executor;
    private final Map<String, ICommand> cmds;

    /**
     * Создает команду помощи.
     * @param cmds отображение названий команд на их реализации
     */
    public Help(Map<String, ICommand> cmds) {
        this.cmds = cmds;
    }

    /**
     * Возвращает название команды.
     * @return строка "help"
     */
    @Override
    public String getName() {
        return "help";
    }

    /**
     * Формирует и возвращает список всех команд с их описаниями.
     * @param args аргументы (не используются)
     * @return отформатированный список команд
     */
    @Override
    public String execute(String args) {
        StringBuilder str = new StringBuilder();
        str.append("Список команд:\n");
        for (Entry<String, ICommand> cmd : cmds.entrySet()) {
            //комманда      - описание (ссылка..getDescription())
            str.append(format("  %-10s - %s%n", cmd.getKey(), cmd.getValue().getDescription()));
        }
        return str.toString();
    }

    /**
     * Возвращает описание команды для справки.
     * @return текстовое описание команды
     */
    @Override
    public String getDescription() {
        return "выводит справку по доступным командам";
    }


}

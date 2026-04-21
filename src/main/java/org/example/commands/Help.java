package org.example.commands;

import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

public class Help implements ICommand {
    //    private CommandExecutor executor;
    private final Map<String, ICommand> cmds;

    public Help(Map<String, ICommand> cmds) {
        this.cmds = cmds;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "help";
    }

    /**
     * @return список всех команд
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
     * @return описание
     */
    @Override
    public String getDescription() {
        return "выводит справку по доступным командам";
    }


}

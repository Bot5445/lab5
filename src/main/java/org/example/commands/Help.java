package org.example.commands;

import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

public class Help implements ICommand{
    private final Map<String,ICommand> cmds;

    public Help(CommandContainer cmds) {
        this.cmds=cmds.getCommands();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String execute() {
        StringBuilder str =new StringBuilder();
        str.append("Список команд:\n");
        for (Entry<String, ICommand> cmd: cmds.entrySet()){
            //комманда      - описание (ссылка..getDescription())
            str.append(format("  %-10s - %s%n", cmd.getKey(), cmd.getValue().getDescription()));
        }
        return str.toString();
    }

    @Override
    public String getDescription() {
        return "выводит справку по доступным командам";
    }


}

package org.example.commands;

import java.util.Map;
import java.util.HashMap;

/**
 * Убрать если получится
 */

public class CommandContainer {
    //хранит команды (название: ссылка)

    private static Map<String, ICommand> commands = new HashMap<>();

    public Map<String, ICommand> getCommands() {
        return commands;
    }

    public void register(ICommand command){
        commands.put(command.getName(), command);
    }
//    public ICommand get(String name){
//        return commands.get(name.toLowerCase());
//    }

    // TODO ...............................
}

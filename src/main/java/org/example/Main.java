package org.example;

import org.example.commands.*;

import org.example.data.Coordinates;
import org.example.data.Location;
import org.example.data.Person;
import org.example.ioStorage.FileStorage;


import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;

public class Main {

    private static final CommandContainer commands = new CommandContainer();

    public static void main(String[] args) {
        collection.put(11222, new Person("qwe", new Coordinates(), 56L, new Location(2, 5.7d)));
        collection.put(11292, new Person("qwe1", new Coordinates(), 56L, new Location(2, 5.7d)));


        registrationCommands();
        FileStorage fileStorage= new FileStorage("file.csv");
        fileStorage.save(collection);
//        collection=fileStorage.load();
//        System.out.println(collection.get(11222).getId());
        Scanner output = new Scanner(System.in);
        while(true){
            System.out.print("> ");
            String line= output.nextLine();
            if (line.isEmpty()) continue;
            String[] tokens = line.split(" ");

//            if ()
            ICommand cmd = commands.getCommands().get(tokens[0].toLowerCase());
            if (cmd == null) continue;

            System.out.println(cmd.execute());



        }
//        while (output.hasNext()) {
//            System.out.print("> ");
//            String line = output.nextLine();
//
//            command.execute();
//        }
    }
    private static void registrationCommands(){
//        static final class CommandContainer {
//            //хранит команды (название: ссылка)
//
//            private static Map<String, ICommand> commands = new HashMap<>();
//
//            public static Map<String, ICommand> getCommands() {
//                return commands;
//            }
//
//            public void register(ICommand command) {
//                commands.put(command.getName(), command);
//            }
//        }
        commands.register(new Help(commands));
        commands.register(new Show(collection));
        commands.register(new Info(collection));
    }
}
package org.example;

import org.example.commands.*;

import org.example.data.ICollManager;
import org.example.data.CollectionManager;
import org.example.ioStorage.FileStorage;
import org.example.ioStorage.IStorage;


import java.util.*;

public class Main {

    private static final Map<String, ICommand> commands = new HashMap<>();


    public static void main(String[] args) {
//        collection.put(11222, new Person("qwe", new Coordinates(), 56L, new Location(2, 5.7d)));
//        collection.put(11292, new Person("qwe1", new Coordinates(), 56L, new Location(2, 5.7d)));

//        try {
//            IStorage fileStorage= new FileStorage();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        collection=fileStorage.load();
        ICollManager collManager = new CollectionManager();
        IStorage storage = new FileStorage();
        Scanner scanner = new Scanner(System.in);

        CommandExecutor executor = new CommandExecutor(Main.commands, scanner);
        ICommand[] cmds = new ICommand[] {
                new Show(collManager),
                new Info(collManager),
                new Insert(collManager),
                new Update(collManager),
                new RemoveKey(collManager),
                new Clear(collManager),
//                new Load(collManager, storage),
                new Save(collManager, storage),
                new Exit(),
                new SumOfHeight(collManager),
                new FilterContainsPassportID(collManager),
                new FilterStartsWithName(collManager),
                new ExecuteScript(executor),
                new RemoveKey(collManager),
                new RemoveLower(collManager),
                new RemoveLowerKey(collManager),
                new ReplaceIfGreater(collManager),
                new Help(Main.commands)
        };
        for (ICommand cmd : cmds) Main.commands.put(cmd.getName(), cmd);
        // Добавляем Help отдельно, если он не был добавлен через массив, или просто добавляем в массив.
        // В данном случае new Help(commands) уже добавит сам себя, если посмотреть код Help,
        // но безопаснее добавить через put:
        boolean exit = false;

        //загрузка данных из файла перед работой
        try {
            System.out.print(new Load(collManager, storage).execute(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        while(!exit){
            System.out.print("> ");

            if (!scanner.hasNextLine()) {// Проверка на (Ctrl+D / Ctrl+Z)
                System.out.println("\nПоток ввода закрыт. Завершение работы...");
                break;
            }
            String strInput = PersonInputReader.cleinerStr(scanner.nextLine());
            if (strInput.isEmpty()) continue;


            try {
                String strOutput = executor.execute(strInput);

                if (strOutput == null) continue; // Пустой ввод
                else if ("exit".equals(strOutput)) {
                    exit = true;
                } else {
                    System.out.println(strOutput);
                }
            }
//            catch (NoSuchElementException e) {
//                System.out.println("\n[EOF] Ввод завершён.");
//                break;
//            }
            catch (IllegalArgumentException e) {
                System.out.println("Ошибка данных: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Критическая ошибка: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("\nВвод завершён.");
    }
}
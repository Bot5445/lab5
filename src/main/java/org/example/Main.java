package org.example;

import org.example.commands.*;

import org.example.data.ICollManager;
import org.example.data.CollectionManager;
import org.example.ioStorage.FileStorage;
import org.example.ioStorage.IStorage;


import java.util.*;

/**
 * Главный класс приложения.
 * Инициализирует компоненты, регистрирует команды и запускает интерактивный цикл обработки команд.
 */
public class Main {

    /**
     * Реестр доступных команд.
     */
    private static final Map<String, ICommand> commands = new HashMap<>();

    /**
     * Точка входа в приложение.
     * Создает менеджер коллекции, настраивает хранилище, регистрирует команды и запускает консольный интерфейс.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        //переменная окружения
        String filePath = System.getenv("LAB5_FILE");
        IStorage storage;
        if (!(filePath == null || filePath.isBlank()))
            storage = new FileStorage(filePath);
        else {
            storage = null;
            System.out.println("Переменная окружения не задана.\n !!Необходима задать переменную окружения для работы с программой!!");
            System.exit(0);
        }

        Scanner scanner = new Scanner(System.in);
        CommandExecutor executor = new CommandExecutor(commands, scanner);

        ICollManager collManager = new CollectionManager();
        ICommand[] cmds = new ICommand[] {
                new Show(collManager),
                new Info(collManager),
                new Insert(collManager),
                new Update(collManager),
                new RemoveKey(collManager),
                new Clear(collManager),
                new Save(collManager, storage),
                new Exit(),
                new SumOfHeight(collManager),
                new FilterContainsPassportID(collManager),
                new FilterStartsWithName(collManager),
                new ExecuteScript(executor, commands),
                new RemoveKey(collManager),
                new RemoveLower(collManager),
                new RemoveLowerKey(collManager),
                new ReplaceIfGreater(collManager),
                new Help(commands),
                new Load(collManager, storage)
        };
        for (ICommand cmd : cmds) commands.put(cmd.getName(), cmd);

        //загрузка данных из файла перед работой
        try {
            System.out.print(new Load(collManager, storage).execute(null));
        } catch (Exception e) {
            System.out.println("Внимание: не удалось загрузить данные — " + e.getMessage());
            System.out.println("Начинаем с пустой коллекцией.");
        }

        boolean exit = false;

        while(!exit){
            System.out.print("> ");

            if (!scanner.hasNextLine()) {// Проверка на (Ctrl+D / Ctrl+Z)
                System.out.println("\nПоток ввода закрыт. Завершение работы...");
                break;
            }
            String strInput = scanner.nextLine();// PersonInputReader.cleinerStr(scanner.nextLine());
            if (strInput == null || strInput.trim().isEmpty()) continue;

            try {
                String strOutput = executor.execute(strInput);

                if (strOutput == null) continue; // Пустой ввод
                else if ("exit".equals(strOutput)) {
                    exit = true;
                } else {
                    System.out.println(strOutput);
                }
            }
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
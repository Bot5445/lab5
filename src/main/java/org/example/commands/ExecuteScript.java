package org.example.commands;

import org.example.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Считает и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде,
 * в котором их вводит пользователь в интерактивном режиме."
 */
public class ExecuteScript implements ICommand {
    private static final Set<String> fileNames = new HashSet<>();
    private final CommandExecutor executor; // Внедряем executor

    public ExecuteScript(CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "execute_script";
    }

    /**
     * @param args имя файла
     * @return ошибку работа с файлом
     * @throws Exception ошибка
     */
    @Override
    public String execute(String args) throws Exception {
        StringJoiner str = new StringJoiner("\n");
        try (
                var file = new Scanner(new File(args))
        ) {
            if (fileNames.contains(args))
                throw new IOException("Файл " + args + "был запрещен, и не может быть запущен повторно");

            fileNames.add(args);
            boolean exit = false;
            while (file.hasNextLine() || !exit) {
                String strInput = file.nextLine();

                // Используем ту же логику, что и в Main
                String strOutput = executor.execute(strInput);

                if (strOutput != null) {
                    str.add(strOutput);
                    if ("exit".equals(strOutput)) exit = true;
                }
            }
            return str.toString();
        } catch (IOException e) {
            return "-".repeat(9) + "\n!!!Файл " + args + "не найден!!!\n" + "-".repeat(9);
        }


    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "считает и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, " +
                "в котором их вводит пользователь в интерактивном режиме.";
    }
}

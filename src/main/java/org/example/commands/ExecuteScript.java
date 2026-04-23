package org.example.commands;

import org.example.CommandExecutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Считает и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде,
 * в котором их вводит пользователь в интерактивном режиме."
 */
public class ExecuteScript implements ICommand {
    private static Set<String> runningFiles = new HashSet<>();
    private final CommandExecutor executor; // Внедряем executor
    private final Map<String,ICommand> commands;

    public ExecuteScript(CommandExecutor executor, Map<String, ICommand> commands) {
        this.executor = executor;
        this.commands = commands;
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
        if (args == null || args.trim().isEmpty()) {
            return "Ошибка: укажите имя файла.";
        }

        // Проверка расширения файла
        if (!args.toLowerCase().endsWith(".txt")) {
            return "Ошибка: можно запускать только скрипты с расширением .txt";
        }
        // Проверка на рекурсию ПЕРЕД открытием файла
        if (runningFiles.contains(args)) {
            return "Ошибка: Обнаружена рекурсия! Файл \"" + args + "\" уже выполняется.";
        }
        StringJoiner str = new StringJoiner("\n");
        // открытие файла
        try (Scanner file = new Scanner(new File(args))) {
            // Регистрируем файл как запущенный
            runningFiles.add(args);

            try {
                while (file.hasNextLine()) {
                    String strInput = file.nextLine();

                    // удаляет комментарии
                    int commentIndex = strInput.indexOf('#');
                    if (commentIndex >= 0) {
                        strInput = strInput.substring(0, commentIndex);
                    }

                    // Удаляем пробелы по краям
                    strInput = strInput.trim();

                    // Если строка пустая (был только комментарий или пустая строка), пропускаем её
                    if (strInput.isEmpty()) continue;

                    //какую строку включаем
                    str.add("Запуск команды: " + strInput);

                    String[] tokens = strInput.split("\\s+", 2);
                    String commandName = tokens[0];

                    // Проверка на интерактивный ввод
                    ICommand cmd = commands.get(commandName);
                    if (cmd != null && cmd.requiresCompoundDataInput()) {
                        str.add("Ошибка: Команда '" + commandName + "' требует интерактивного ввода и не может быть выполнена в скрипте.");
                        continue;
                    }

                    String strOutput = executor.execute(strInput);

                    if (strOutput != null) {
                        str.add(strOutput);
                        if ("exit".equals(strOutput)) break;
                    }
                }
                return str.toString();
            } finally {
                // 4. ГАРАНТИРОВАННО удаляем файл из списка активных после выполнения
                // Это позволяет запускать скрипт повторно после завершения
                runningFiles.remove(args);
            }
        } catch (FileNotFoundException e) {
            return "-".repeat(9) + "\n!!!Файл \"" + args + "\" не найден!!!\n" + "-".repeat(9);
        } catch (IOException e) {
            return "Ошибка ввода-вывода: " + e.getMessage();
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

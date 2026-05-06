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
    private final Map<String, ICommand> commands;

    /**
     * Создает команду выполнения скрипта.
     *
     * @param executor исполняющий объект для запуска команд из скрипта
     * @param commands мапа доступных команд для проверки на требования интерактивного ввода
     */
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
     * Построчно читает файл, игнорируя комментарии (начинающиеся с #),
     * и выполняет команды. Команды, требующие интерактивного ввода ({@link ICommand#requiresCompoundDataInput()}),
     * пропускаются. Ошибка в одной команде не прерывает выполнение скрипта.
     *
     * @param args путь к файлу скрипта (обязательно с расширением .txt)
     * @return лог выполнения команд и их результатов
     * @throws Exception при ошибках ввода-вывода
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

                    try {
                        String strOutput = executor.execute(strInput);
                        if (strOutput != null) {
                            str.add(strOutput);
                            if ("exit".equals(strOutput)) break;
                        }
                    } catch (Exception e) {
                        // Ошибка в одной команде не ломает весь скрипт
                        str.add("Ошибка: " + e.getMessage());
                    }
                }
                return str.toString();
            } finally {
                // удаление файла из списка после выполнения, для повторного запуска скрипта после завершения
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

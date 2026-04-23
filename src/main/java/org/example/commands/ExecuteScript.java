package org.example.commands;

import org.example.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Считает и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде,
 * в котором их вводит пользователь в интерактивном режиме."
 */
public class ExecuteScript implements ICommand {
    private static final Set<String> fileNames = new HashSet<>();
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
        StringJoiner str = new StringJoiner("\n");
        try (var file = new Scanner(new File(args))) {

            if (fileNames.contains(args))
                throw new IOException("Файл " + args + " был запрещен один раз, и не может быть запущен повторно");

            fileNames.add(args);

            while (file.hasNextLine()) {
                String strInput = file.nextLine();

                int commentIndex = strInput.indexOf('#');
                if (commentIndex >= 0) {
                    strInput = strInput.substring(0, commentIndex);
                }

                // 2. Удаляем пробелы по краям
                strInput = strInput.trim();

                // 3. Если строка пустая (был только комментарий или пустая строка), пропускаем её
                if (strInput.isEmpty()) {
                    continue;
                }
                String[] tokens = strInput.split("\\s+", 2);
                String commandName = tokens[0];

                // 3. Проверка на интерактивный ввод
                ICommand cmd = commands.get(commandName);
                if (cmd != null && cmd.requiresCompoundDataInput()) {
                    str.add("Ошибка: Команда '" + commandName + "' требует интерактивного ввода и не может быть выполнена в скрипте.");
                    continue; // Пропускаем команду
                }

                String strOutput = executor.execute(strInput);

                if (strOutput != null) {
                    str.add(strOutput);
                    if ("exit".equals(strOutput)) break;
                }
            }
            return str.toString();
        } catch (IOException e) {
            return "-".repeat(9) + "\n!!!Файл \"" + args + "\" не найден!!!\n" + "-".repeat(9);
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

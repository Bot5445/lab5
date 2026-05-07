package org.example;

import org.example.commands.ICommand;

import java.util.Map;
import java.util.Scanner;


/**
 * Разбивает строку на "команда [аргументы]" и запускает команду по названии команды с аргуменами.
 */
public final class CommandExecutor {
    private final Map<String, ICommand> commands;
    private  final Scanner scanner;

    /**
     * Создает экземпляр исполнителя команд.
     * @param commands отображение названий команд (ключ) на их реализации (значение)
     * @param scanner сканер для чтения ввода из консоли (используется для интерактивного опроса)
     */
    public CommandExecutor(Map<String, ICommand> commands, Scanner scanner) {
        this.commands = commands;
        this.scanner = scanner;
    }

    /**
     * Выполняет команду по строке ввода.
     * Разбирает строку, находит соответствующую команду в реестре и запускает её выполнение.
     * При этом производится очистка ввода от лишних пробелов и управляющих символов.
     * @param input строка из консоли, содержащая имя команды и аргументы
     * @return результат выполнения команды, сообщение об ошибке или null, если ввод пустой
     * @throws Exception если произошла ошибка при выполнении команды или при интерактивном вводе данных
     */
    public String execute(String input) throws Exception {
        if (input == null) return null;

        //Удаляем невидимые управляющие символы (кроме пробела и перевода строки)
        // \p{Cntrl} - это контрольные символы (ASCII 0-31 и 127).
        // Они могут возникнуть при копировании или ошибках ввода.
        //"\\s+" Заменяем множество пробелов на один и убираем пробелы по краям
        String cleanInput = input.replaceAll("\\p{Cntrl}", " ")
                                .trim()
                                .replaceAll("\\s+", " ");
        if (cleanInput.isEmpty()) return null;

        // "\\s+" разбивает по пробелам, игнорируя их количество.
        // разбивает на "команда и аргументы".
        String[] tokens = cleanInput.split("\\s+", 2);
        String commandName = tokens[0];

        ICommand cmd = commands.get(commandName);
        if (cmd == null) {
            return "Команда \"" + commandName + "\" не найдена";
        }

        String args = (tokens.length > 1) ? tokens[1] : null;

        if (!cmd.acceptsArguments() && args != null && !args.trim().isEmpty()) {
            return "Ошибка: команда \"" + commandName + "\" не принимает аргументов.";
        }

        PersonInputReader personReader = new PersonInputReader(scanner);
        if (cmd.requiresCompoundDataInput()) {
            // Ридер возвращает готовую CSV-строку. Мы просто подменяем аргументы.
            args = personReader.readPersonData(args);
        }
        return cmd.execute(args);
    }
}

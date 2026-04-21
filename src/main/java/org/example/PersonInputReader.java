package org.example;

import org.example.data.Color;
import org.example.data.Country;
import org.example.data.PersonFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonInputReader {
    private final Scanner scanner;

    public PersonInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Формирует данные Person в формате CSV.
     * @param args Строка с аргументами (может быть null). Если не null, поля заполняются из неё с пропуском интерактивного ввода.
     * @return CSV строка с данными Person.
     */
    public String readPersonData(String args) {
        StringJoiner dataStr = new StringJoiner(",");

        Queue<String> argQueue = new LinkedList<>();


        if (args != null && !args.isBlank()) {
            Collections.addAll(argQueue, args.split("[,\\s]+"));
        }

        // 1. ID
        dataStr.add(null);

        // 2. Имя
        dataStr.add(readNextArgOrPrompt(argQueue, "Имя (латиница)", isString()));

        // 3-4. Координаты
        dataStr.add(readField("Координата X (float)", isFloat(x -> true), ".-"));
        dataStr.add(readField("Координата Y (float)", isFloat(y -> true), ".-"));

        // 5. Дата (Авто-генерация)
        dataStr.add(null);

        // 6. Рост
        dataStr.add(readNextArgOrPrompt(argQueue, "Рост (Long > 0)",
                isNumber(Long::parseLong, h -> h > 0), "-"));

        // 5. PassportID (Nullable)
        dataStr.add(readNextArgOrPrompt(argQueue, "PassportID (пусто = null)", isPassportId()));
        // 8. Enum Цвет
        dataStr.add(readFieldEnum("Цвет волос", Color.class));

        // 9. Enum Страна
        dataStr.add(readFieldEnum("Национальность", Country.class));

        // 10-12. Локация
        dataStr.add(readField("Локация X (long)", isNumber(Long::parseLong), "-"));
        dataStr.add(readField("Локация Y (double)", isDouble(y -> true), ".-"));
        dataStr.add(readField("Название локации (пусто = null)",  s -> {
            try {
                PersonFactory.validateAndParseName(s);
                return true;
            } catch (Exception e) {
                return false;
            }
        }));

        return dataStr.toString();
    }

    private String readField(String prompt, Predicate<String> validator) {
        return readField(prompt, validator, "");
    }
    /**
     * Запрашивает данные у пользователя с поддержкой доп. символов при очистке.
     * @param formatNumber строка с символами, которые нужно сохранить в числе (например, ".-")
     * @param prompt тест для вывода сообщение пользователю
     * @param validator условие
     * @return полученные данные от пользователя
     */
    private String readField(String prompt, Predicate<String> validator, String formatNumber) {
        while (true) {
            System.out.print(prompt + ": ");
            String rawInput = scanner.nextLine();  // сохраняем "как ввёл"
            String cleaned = cleinerStr(rawInput, formatNumber);

            if (validator.test(cleaned)) {
                return cleaned;
            }

            // 👇 Показываем, что именно было введено и почему не подошло
            System.out.println("Неверный формат: \"" + rawInput.trim() + "\". Попробуйте снова.");
        }
    }

    /**
     * Пытается взять значение из очереди аргументов. Если очередь пуста или значение не прошло валидацию,
     * запрашивает ввод у пользователя.
     * @param argQueue полученная строка для пользователя
     * @param prompt сообщение для пользователя
     * @param validator условие правильности данных
     * @return полученное сообщение
     */
    private String readNextArgOrPrompt(Queue<String> argQueue, String prompt, Predicate<String> validator) {
        return readNextArgOrPrompt(argQueue, prompt, validator, "");
    }
    /**
     * Пытается взять значение из очереди аргументов, с информативной ошибкой.
     * @param argQueue полученная строка для пользователя
     * @param prompt сообщение для пользователя
     * @param validator условие правильности данных
     * @param formatNumber строка с символами, которые нужно сохранить в числе (например, ".-")
     * @return полученное сообщение
     */
    private String readNextArgOrPrompt(Queue<String> argQueue, String prompt,
                                       Predicate<String> validator, String formatNumber) {
        if (!argQueue.isEmpty()) {
            String val = argQueue.poll();
            String cleaned = cleinerStr(val, formatNumber); // ← очищаем с учётом формата
            if (validator.test(val)) {
                return cleaned;
            }
            // Если аргумент был, но он невалиден, мы не падаем, а переспрашиваем у пользователя (или можно выбросить ошибку)
            System.out.println("Значение для \"" + prompt + "\" не правильно. Требуется ручной ввод.");
        }
        return readField(prompt, validator, formatNumber);
    }

    /**
     * Аналогичен readNextArgOrPrompt, но для Enum-полей с выводом доступных значений.
     * @param fieldName сообщение для пользователя
     * @param enumClass Enum
     * @return полученное сообщение
     */
    private <E extends Enum<E>> String readFieldEnum(String fieldName, Class<E> enumClass) {
        String availableValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        String prompt = fieldName + " (доступно: " + availableValues + ")";

        Predicate<String> enumValidator = s -> {
            if (s == null || s.isEmpty()) return true; // Nullable
            try {
                Enum.valueOf(enumClass, s.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        };

        return readField(prompt, enumValidator);
    }

    private static Predicate<String> isString() {
        return s -> {
            try {
                PersonFactory.validateAndParseName(cleinerStr(s));
                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }

    private static <T extends Number> Predicate<String> isNumber(
            Function<String, T> parser,
            Predicate<T> condition,
            boolean allowDecimal) {

        return s -> {
            if (s == null || s.isBlank()) {
                System.out.println("Поле не может быть пустым.");
                return false;
            }

            String normalized = s.trim().replace(',', '.');

            // 1️ Сначала проверяем формат строки
            if (!isValidNumberFormat(normalized, allowDecimal)) {
                System.out.println("Неверный формат числа: \"" + s.trim() + "\". " +
                        "Используйте только цифры, точку (.) и знак минус (-) в начале.");
                return false;
            }

            // 2 Только потом пытаемся распарсить
            try {
                T value = parser.apply(normalized);
                if (!condition.test(value)) {
                    System.out.println("Значение \"" + s.trim() + "\" не удовлетворяет условию (например, должно быть > 0).");
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка преобразования числа: \"" + s.trim() + "\".");
                return false;
            }
        };
    }

    // Удобные перегрузки для вызова
    private static <T extends Number> Predicate<String> isNumber(Function<String, T> parser) {
        return isNumber(parser, v -> true, false); // по умолчанию: целое, без условий
    }
    private static <T extends Number> Predicate<String> isNumber(Function<String, T> parser, Predicate<T> condition) {
        return isNumber(parser, condition, false); // целое с условием
    }
    private static Predicate<String> isFloat(Predicate<Float> condition) {
        return isNumber(Float::parseFloat, condition, true); // дробное
    }
    private static Predicate<String> isDouble(Predicate<Double> condition) {
        return isNumber(Double::parseDouble, condition, true); // дробное
    }
    /**
     * Базовая очистка для текстовых полей (без спецсимволов).
     */
    public static String cleinerStr(String input) {
        return cleinerStr(input, "");
    }

    /**
     * Очищает строку:
     * 1. Заменяет запятую на точку (для дробных чисел).
     * 2. Удаляет управляющие символы.
     * 3. Оставляет только буквы, цифры, пробелы и указанные доп. символы.
     * 4. Схлопывает пробелы.
     * @param input исходная строка
     * @param formatNumber строка с доп. символами, которые нужно сохранить (например, ".-")
     */
    public static String cleinerStr(String input, String formatNumber) {
        if (input == null) return null;

        // 1. Нормализация десятичного разделителя: запятая → точка
        String normalized = input.replace(',', '.');

        // 2. Экранируем спецсимволы для безопасной вставки в класс символов []
        // Точка и дефис имеют спец.значение в регексе, поэтому экранируем их
        String safeFormat = formatNumber.replaceAll("([.\\-\\[\\]^\\\\])", "\\\\$1");

        // 3. Фильтр: разрешаем буквы (\p{L}), цифры (\p{N}), пробелы (\s) и доп. символы
        String filter = "[^\\p{L}\\p{N}\\s" + safeFormat + "]";

        return normalized.replaceAll("\\p{Cntrl}", " ")  // управляющие → пробел
                .replaceAll(filter, "")                   // удаляем запрещённые символы
                .trim()                                   // обрезаем края
                .replaceAll("\\s+", " ");                 // схлопываем множественные пробелы
    }
    private static <T extends Number> Predicate<String> isNumberWithMessage(
        Function<String, T> parser,
        Predicate<T> condition,
        String fieldName) {

        return s -> {
            if (s == null || s.isBlank()) {
                System.out.println("Поле \"" + fieldName + "\" не может быть пустым.");
                return false;
            }
            try {
                T value = parser.apply(s.trim());
                if (!condition.test(value)) {
                    System.out.println("Значение \"" + s + "\" не удовлетворяет условию (например, должно быть > 0).");
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                System.out.println("\"" + s + "\" не является корректным числом.");
                return false;
            }
        };
    }
    /**
     * Проверяет, является ли строка корректным числом в формате:
     * [-]цифры[.цифры] или [-].цифры
     * Примеры: "123", "-45.67", ".5", "-0.001"
     * @param s строка
     * @param allowDecimal условие для числа
     * @return boolean
     */
    private static boolean isValidNumberFormat(String s, boolean allowDecimal) {
        if (s == null || s.isBlank()) return false;

        String normalized = s.trim().replace(',', '.');

        // Регулярка: опциональный минус, затем либо "123", либо "123.45", либо ".45"
        return normalized.matches(allowDecimal
                ? "^-?(\\d+\\.?\\d*|\\.\\d+)$"   // дробное: 123, 123.45, .45, -0.5
                : "^-?\\d+$");                   // целое: 123, -45
    }

    /**
     * Валидатор для PassportID:
     * - пустая строка или пробелы → null (допустимо)
     * - иначе: любые буквы и цифры (настройте regex под требования вашей лабы)
     * @return boolean
     */
    private static Predicate<String> isPassportId() {
        return s -> {
            if (s == null || s.isBlank()) return true; // пустота = null
            // Пример: только латиница и цифры, длина 1-32 символа
            return s.trim().matches("^[A-Za-z0-9]{1,32}$");
        };
    }
}
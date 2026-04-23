package org.example.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Создает объект Person из строки данных.
 * Формат ввода: name, coordX, coordY,height,passportID,hairColor,nationality,locX,locY,locName
 */
public class PersonFactory {
    // Изменяем формат на день.месяц.год (например, 25.12.2023)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    static {
        // Включаем строгий разбор дат.
        // Если пользователь введет 32.13.2023, выбросит ParseException,
        // вместо попытки интерпретировать дату (например, 32 января -> 1 февраля).
        DATE_FORMAT.setLenient(false);
    }

    public static Integer validateAndParseId(String input) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty() || input.equalsIgnoreCase("null")) return null;
        try {
            int id = Integer.parseInt(input.trim());
            if (id <= 0) throw new IllegalArgumentException("ID должен быть > 0");
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID должен быть целым числом!!" + e);
        }
    }

    public static String validateAndParseName(String input) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым");
        String name = input.trim();
        // Разрешаем латиницу, дефис и символ &
        if (!name.matches("^[a-zA-Z&\\-]+$")) {
            throw new IllegalArgumentException("Имя \""+name+"\" должно содержать только латиницу, дефис и символ &");
        }
        return name;
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty() || str.equalsIgnoreCase("null")) return null;
        try {
            return Enum.valueOf(enumClass, str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Недопустимое значение '" + str.trim() + "'. Ожидается одно из: "
                    + Arrays.toString(enumClass.getEnumConstants()), e);
        }
    }

    /**
     * Парсит дату из строки.
     * Если строка "null", возвращает текущую дату.
     * Ожидает формат dd.MM.yyyy
     */
    private static Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equalsIgnoreCase("null")) return new Date();

        try {
            // Парсим. setLenient(false) уже проверит существование даты (високосный год и т.д.)
            Date date = DATE_FORMAT.parse(dateStr.trim());
            if (date.after(new Date())) throw new IllegalArgumentException("Дата не может быть в будущем");
            return date;
        } catch (ParseException e) {
            throw new ParseException("Формат даты: dd.MM.yyyy", e.getErrorOffset());
        }
    }

    public static Float parseFloat(String input, String fieldName) throws NumberFormatException {
        if (input == null || input.trim().isEmpty())
            throw new NumberFormatException(fieldName + " не может быть пустым");
        return Float.parseFloat(input.trim());
    }

    /**
     * ввод от пользователя insert Name Height [Passport]
     * опрос пользователя на ввод данных: CoordX CoordY [Color] [Country] LocX LocY [LocName]
     * @param str данные в формате CSV
     * @return Person
     */
    public static Person createFromStringArray(String[] str) throws IllegalArgumentException, ParseException {
        Integer id = validateAndParseId(str[0]);
        if (id == null) {
            id = Math.abs(new java.util.Random().nextInt(100000)) + 1;
        }

        String name = validateAndParseName(str[1]);

        // Coordinate
        Float x = parseFloat(str[2], "Координата X");
        Float y = parseFloat(str[3], "Координата Y");

        Date creationDate = parseDate(str[4]);

        Long height = Long.parseLong(str[5].trim());
//        try {
//            height = Long.valueOf(str[5].trim());
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("В столбце Height: "+e.getMessage());
//        }

        String passportID = (str[6] == null || str[6].isBlank() || str[6].trim().equalsIgnoreCase("null"))
                ? null
                : str[6].trim();

        Color hairColor = parseEnum(Color.class, str[7]);
        Country nationality = parseEnum(Country.class, str[8]);

        long locX = Long.parseLong(str[9].trim());
        double locY = Double.parseDouble(str[10].trim());
        String locName = (str[11] == null || str[11].isBlank() || str[11].trim().equalsIgnoreCase("null"))
                ? null
                : str[11].trim();
        Location location = new Location(locX, locY);
        location.setName(locName);

        return new Person(
                id, name,
                new Coordinates(x, y),
                creationDate, height, passportID,
                hairColor, nationality,
                location
        );
    }

    /**
     * Метод для форматирования даты при сохранении в файл (используется в Person.toString())
     */
    public static String formatDate(Date date) {
        if (date == null) return "null";
        return DATE_FORMAT.format(date);
    }
}


package org.example.ioStorage;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.example.data.PersonFactory;
import org.example.data.Person;

import java.io.*;

import java.text.ParseException;
import java.util.*;

/**
 * Реализация хранилища данных на основе CSV-файлов.
 * Предоставляет методы для сохранения и загрузки коллекции объектов {@link Person}.
 */
public class FileStorage implements IStorage {

    private String fileName;

    /**
     * Устанавливает имя файла для хранилища.
     * Если имя не содержит расширения, автоматически добавляет ".csv".
     *
     * @param fileName имя файла (с путем или без)
     */
    public void setFileName(String fileName) {
        if (fileName.contains(".")) {
            if (!fileName.endsWith(".csv")) {
                fileName = fileName + ".csv";
            }
        }
        this.fileName = fileName;
    }

    /**
     * Создает объект FileStorage с именем файла по умолчанию ("file.csv").
     */
    public FileStorage() {
        this.fileName = "file.csv";
    }

    /**
     * Создает объект FileStorage с указанным именем файла, добавляя расширение ".csv".
     *
     * @param fileCSV имя файла без расширения
     */
    public FileStorage(String fileCSV) {
        fileName = fileCSV;

    }

    /**
     * Загружает коллекцию объектов Person из CSV-файла.
     * Парсит файл построчно, игнорируя пустые строки.
     * Ошибки парсинга конкретных строк выводятся в стандартный поток ошибок (stderr).
     *
     * @return список загруженных объектов Person
     * @throws IllegalArgumentException если файл не найден, или произошла критическая ошибка чтения/формата
     */
    @Override
    public List<Person> load() throws IllegalArgumentException {
        List<Person> people = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            StringJoiner errorString = new StringJoiner("\n");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = parser.parseLine(line);
                try {
                    Person person = PersonFactory.createFromStringArray(parts);
                    people.add(person);
                } catch (IllegalArgumentException e) {
                    errorString.add(e.getMessage());
                }
                System.err.println(errorString);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл данных не найден (" + fileName + ").", e);
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("Ошибка загрузки файла: " + e.getMessage());
        }
        return people;
    }

    /**
     * Сохраняет коллекцию объектов Person в CSV-файл.
     * Запись производится с использованием буферизации.
     *
     * @param persons коллекция объектов для сохранения
     * @throws IOException если файл не найден, нет прав на запись или произошла ошибка ввода-вывода
     */
    @Override
    public void save(Collection<Person> persons) throws IOException {
        try (
                BufferedOutputStream bufferOut = new BufferedOutputStream(new FileOutputStream(fileName))
        ) {
            for (Person entry : persons) {
                String row = entry.toString() + "\n";
                bufferOut.write(row.getBytes());
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Файл не найден или нет прав для записи: " + fileName);
        } catch (IOException e) {
            throw new IOException("Ошибка сохранения в " + fileName + ": " + e.getMessage());
        }

    }
}

package org.example.ioStorage;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.example.data.PersonFactory;
import org.example.data.Person;

import java.io.*;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.util.*;

public class FileStorage implements IStorage{

    private String fileName;

    public void setFileName(String fileName) {
        if (fileName.contains(".")){
            if (!fileName.endsWith(".csv")){
                fileName = fileName + ".csv";
            }else {
                fileName = fileName;
            }
        }
        this.fileName = fileName;
    }

    public FileStorage() {
        this.fileName = "file.csv";
    }

    public FileStorage(String fileCSV) {
        fileName =fileCSV+".csv";
    }
    @Override
    public List<Person> load() throws IllegalArgumentException {
        List<Person> people = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        try (var file = new Scanner(new File(fileName))) {
            StringJoiner errorString = new StringJoiner("\n");
            while (file.hasNextLine()) {
                String line = file.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = parser.parseLine(line);
                try {
                    Person person = PersonFactory.createFromStringArray(parts);
                    people.add(person);
                    //TODO сделать вывод ошибки
                } catch (IllegalArgumentException e) {
                    errorString.add(e.getMessage());
                }
                System.err.println(errorString);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден: " + fileName, e);
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("Ошибка загрузки файла: " + e.getMessage());
        }
        return people;
    }
    @Override
    public void save(Collection<Person> persons) throws IOException{
        try (
        BufferedOutputStream bufferOut = new BufferedOutputStream(new FileOutputStream(fileName))
        ){
            for (Person entry : persons) {
                String row = entry.toString() + "\n";
                bufferOut.write(row.getBytes());
            }
        } catch (AccessDeniedException e){
            throw new IOException("Нет прав на запись: " + fileName, e);
        }

    }
}

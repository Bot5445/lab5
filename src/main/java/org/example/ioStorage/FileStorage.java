package org.example.ioStorage;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.example.data.Person;

import java.io.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class FileStorage implements IStorage{
    private final String fileCSV;

    public FileStorage(String fileCSV) {
        this.fileCSV = fileCSV;
    }

    public Map<Integer, Person> load() {
        Map<Integer, Person> DataFile = new TreeMap<>();
        String str = null;

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        try (var file = new Scanner(new File(fileCSV))
        ) {
            while (file.hasNextLine()) {
                String line = file.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = parser.parseLine(line);


//                System.out.println(Arrays.toString(parts));
//                String[] split = line.split(", ",2);
                // TODO use openCSV or anything

//                DataFile.put(Integer.valueOf(split[0]), split[1]);??2
//                System.out.println(Arrays.toString(split));

            }

        } catch (IOException e) {
            System.out.println("-".repeat(9) + "\n!!!Файл не найден!!!\n" + "-".repeat(9));
        }
        return DataFile;
    }

    public void save(Map<Integer, Person> persons) {
        try {
            //OutputStreamWriter file = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(fileCSV)));
            BufferedOutputStream bufferOut = new BufferedOutputStream(new FileOutputStream("file2.csv"));
            String row;

            for (Entry<Integer, Person> entry : persons.entrySet()) {
                row = String.valueOf(entry.getValue().toString() + "\n");
                bufferOut.write(row.getBytes());
            }
            bufferOut.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
// персон с ид 1
// имя апапапа
// возраст 22

// 1;john;22
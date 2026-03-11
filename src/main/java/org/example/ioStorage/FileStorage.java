package IOStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedOutputStream;



import java.util.Map;

public class FileStorage implements IStorage{

    @Override
    public void load(Map<String, String> x) {
        try {
            Scanner x = new Scanner(new File("file.csv"));
        } catch (FileNotFoundException e) {
            System.out.println("-"*9,"\n!!!Файл не найден!!!\n", "-"*9);
        }
    }

    @Override
    public void save(Map<String, String> x) {

    }
}

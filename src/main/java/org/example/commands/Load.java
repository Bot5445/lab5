package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;
import org.example.ioStorage.IStorage;

import java.util.List;

/**
 * Загружает файл, можно указать имя файла загрузки
 */
public class Load implements ICommand{
    private final IGetterSetter collectionManager;
    private final IStorage storage;

    public Load(IGetterSetter collectionManager, IStorage storage) {
        this.collectionManager = collectionManager;
        this.storage = storage;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "load";
    }

    /**
     * @return количество person загруженных из файла
     */
    @Override
    public String execute(String args) throws Exception {
        // Storage возвращает список
        List<Person> loadedPeople = storage.load();

        collectionManager.setPersons(loadedPeople);

        return "Коллекция загружена (" + loadedPeople.size() + " элементов)\n";
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "загружает файл, можно указать имя файла загрузки";
    }
}

package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.ioStorage.IStorage;

/**
 * Сохраняет файл, можно указать имя файла сохранения
 */
public class Save implements ICommand{
    private final IGetterSetter collectionManager;
    private final IStorage storage;

    public Save(IGetterSetter collectionManager, IStorage storage) {
        this.collectionManager = collectionManager;
        this.storage = storage;
    }

    /**
     * @return название
     */
    @Override
    public String getName() {
        return "save";
    }

    /**
     * @param args имя файла
     * @return сохранена ли коллекция
     */
    @Override
    public String execute(String args) throws Exception {
        if (args!=null){
            storage.setFileName(args);
        }
        storage.save(collectionManager.getAllPersons());
        return "Коллекция сохранена";
    }

    /**
     * @return описание
     */
    @Override
    public String getDescription() {
        return "сохраняет файл, можно указать имя файла сохранения";
    }
}

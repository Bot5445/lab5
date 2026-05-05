package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.ioStorage.IStorage;

/**
 * Сохраняет текущую коллекцию в файл через реализацию {@link IStorage}.
 * Позволяет указать новое имя файла для сохранения.
 */
public class Save implements ICommand{
    private final IGetterSetter collectionManager;
    private final IStorage storage;

    /**
     * @param collectionManager менеджер для получения текущей коллекции
     * @param storage объект хранения для выполнения сохранения
     */
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
     * Сохраняет коллекцию. Если переданы аргументы, они используются как новое имя файла.
     * @param args (опционально) новое имя файла для сохранения
     * @return статус выполнения сохранения
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

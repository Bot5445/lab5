package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;
import org.example.ioStorage.IStorage;

import java.util.List;

/**
 * Загружает текущую коллекцию в файл через реализацию {@link IStorage}.
 * Позволяет указать новое имя файла для загрузки.
 */
public class Load implements ICommand{
    private final IGetterSetter collectionManager;
    private final IStorage storage;

    /**
     * Создает команду загрузки.
     * @param collectionManager менеджер коллекции, в который будут загружены данные
     * @param storage объект хранилища для чтения файла
     */
    public Load(IGetterSetter collectionManager, IStorage storage) {
        this.collectionManager = collectionManager;
        this.storage = storage;
    }

    /**
     * Возвращает название команды.
     * @return строка "load"
     */
    @Override
    public String getName() {
        return "load";
    }

    /**
     * Загружает коллекцию из файла и обновляет данные в менеджере.
     * @param args аргументы (не используются в текущей реализации)
     * @return статус выполнения загрузки с количеством элементов
     * @throws Exception при ошибке чтения файла или парсинга данных
     */
    @Override
    public String execute(String args) throws Exception {
        // Storage возвращает список
        List<Person> loadedPeople = storage.load();

        collectionManager.setPersons(loadedPeople);

        return "Коллекция загружена (" + loadedPeople.size() + " элементов)\n";
    }

    /**
     * Возвращает описание команды для справки.
     * @return текстовое описание
     */
    @Override
    public String getDescription() {
        return "загружает файл, можно указать имя файла загрузки";
    }
}

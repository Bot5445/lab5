package org.example.commands;

import org.example.data.IGetterSetter;
import org.example.data.Person;
import org.example.ioStorage.IStorage;

import java.util.List;
import java.util.Map;

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
    // Если указан аргумент - устанавливаем новое имя файла

        if (args != null && !args.trim().isEmpty()) {
        storage.setFileName(args.trim().replace("\"", ""));
    }

    // Загружаем список из файла
    List<Person> loadedPeople = storage.load();

    // Получаем текущую карту коллекции
    // но getPerson() возвращает ссылку на саму Map.
    Map<Integer, Person> currentCollection = collectionManager.getPerson();
    if (currentCollection == null) {
        collectionManager.setPersons(loadedPeople);
        return "Коллекция загружена (" + loadedPeople.size() + " элементов)\n";
    }
    // Объединяем
    int count = 0;
    for (Person p : loadedPeople) {
        // put заменит элемент, если ID совпадает, или добавит новый
        currentCollection.put(p.getId(), p);
        count++;
    }
        return "Коллекция объединена с файлом. Обработано элементов: " + count + "\n";
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

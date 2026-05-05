package org.example.ioStorage;

import org.example.data.Person;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для операций ввода-вывода коллекции (сохранение и загрузка из внешнего источника, например, файла).
 */
public interface IStorage {
    /**
     * Загружает коллекцию из источника.
     * @return список загруженных объектов {@link Person}
     * @throws IOException при ошибке чтения
     */
    List<Person> load() throws Exception;

    /**
     * Сохраняет коллекцию в источник.
     * @param persons коллекция объектов для сохранения
     * @throws IOException при ошибке записи
     */
    void save(Collection<Person> persons) throws IOException;

    /**
     * Устанавливает имя файла (или пути) для операций ввода-вывода.
     * @param fileName новое имя файла
     */
    void setFileName(String fileName);
}

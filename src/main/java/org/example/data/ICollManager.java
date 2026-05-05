package org.example.data;

/**
 * Главный интерфейс менеджера коллекции.
 * Объединяет функциональность CRUD-операций, доступа к данным, удаления по ключу и проверки ключей.
 */
public interface ICollManager extends ICrud, IGetterSetter, IRemoveKey, IsKey {
}


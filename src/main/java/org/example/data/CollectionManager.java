package org.example.data;

import java.util.*;
import java.util.Map.Entry;

public class CollectionManager implements ICollManager {
    private final Map<Integer, Person> collection = new TreeMap<>();


    /**
     * @return коллекцию
     */
    @Override
    public Map<Integer, Person> getPerson() {
        return collection;
    }

    @Override
    public void addPerson(Person person) {
        collection.put(person.getId(), person);
    }

    @Override
    public void deletePerson(Integer id) {
        collection.remove(id);
    }

    @Override
    public void updatePerson(Integer id, Person person) {
        if (collection.containsKey(id)){
            collection.remove(id);
            collection.put(id, person);
        } else {
            throw new IllegalArgumentException("No id");
        }

    }

    @Override
    public void clear() {
        collection.clear();
    }

    /**
     * @param id ID человека
     * @return есть ли человек в коллекции
     */
    @Override
    public boolean containsId(Integer id) {
        return collection.containsKey(id);
    }

    /**
     * @param id ID человека
     * @return Person по ID
     */
    @Override
    public Person getPersonById(Integer id) {
        return collection.get(id);
    }

    /**
     * @param persons заново создает коллекцию
     */
    @Override
    public void setPersons(List<Person> persons) {
        collection.clear();
        for (Person p : persons) {
            collection.put(p.getId(), p);
        }
    }

    /**
     * @return получить всю коллекцию
     */
    @Override
    public Collection<Person> getAllPersons() {
        return collection.values(); // Возвращает коллекцию значений напрямую
    }

    /**
     * @return пустая ли коллекция
     */
    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public int removeLower(Person template) {
        return removeLowerKey(template.getId());
    }

    @Override
    public int removeLowerKey(int thresholdId) {
        int removedCount = 0;

        Iterator<Entry<Integer, Person>> iterator = collection.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Integer, Person> entry = iterator.next();

            // Сравниваем ключи
            if (entry.getKey() < thresholdId) {
                iterator.remove();
                removedCount++;
            }
        }
        return removedCount;
    }

    @Override
    public String toString() {
        String str = "";
        for (Entry<Integer, Person> entry : collection.entrySet()) {
            str += entry.getKey() + ": " + entry.getValue().toString() + "\n";
        }
        return str;
    }

}

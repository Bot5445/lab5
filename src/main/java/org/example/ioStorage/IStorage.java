package org.example.ioStorage;

import org.example.data.Person;

import java.util.Map;

public interface IStorage {
    Map<Integer, Person> load();
    void save(Map<Integer, Person> x);
}

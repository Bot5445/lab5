package org.example.ioStorage;

import org.example.data.Person;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IStorage {
    void setFileName(String fileName);
    List<Person> load() throws Exception;
    void save(Collection<Person> persons) throws IOException;
}

package org.example.data;

public interface ICrud {
    void addPerson(Person person);

    void deletePerson(Integer id);

    void updatePerson(Integer id, Person person);

    void clear();
}

package org.example.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IGetterSetter {
    Map<Integer, Person> getPerson();

    Person getPersonById(Integer id);

    Collection<Person> getAllPersons();

    void setPersons(List<Person> persons);
}

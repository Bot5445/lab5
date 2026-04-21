package org.example.data;

import java.io.Serializable;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
public final class Person implements Serializable {
    //    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Long height; //Поле не может быть null, Значение поля должно быть больше 0

    @Setter
    private String passportID; //Поле может быть null
    @Setter
    private Color hairColor; //Поле может быть null
    @Setter
    private Country nationality; //Поле может быть null
    @NonNull
    private final Location location; //Поле не может быть null

    public Person(Integer id, String name, Coordinates coordinates, Long height, Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location не может быть null");
        }
        this.id = id; //abs((new Random()).nextInt())
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.height = height;
        this.location = location;
    }

    @Override
    public String toString() {
        String hairColor = "null";
        if (this.hairColor != null) {
            hairColor = this.hairColor.toString();
        }
        String nationality = "null";
        if (this.nationality != null) {
            nationality = this.nationality.toString();
        }
        // Используем PersonFactory для форматирования даты
        String dateStr = PersonFactory.formatDate(this.creationDate);

        return id.toString() + "," + name + "," + coordinates.toString() + ","
                + dateStr + ","+ height.toString()+","+passportID +","
                + hairColor + "," + nationality + "," + location;
    }
    /**
     * @return  все поля Person
     */
    public static String[] getHeaders(){
//        return new String[]{"id", "name", Coordinates.toStringsArray()[0], Coordinates.toStringsArray()[1], "creationDate", "height", "passportID", "hairColor", "nationality", "location"};
        return concat(
            new String[]{"id", "name"},
            Coordinates.toStrings(),
            new String[]{"creationDate", "height", "passportID", "hairColor", "nationality"},
            Location.toStrings());
    }

    private static String[] concat(String[]... arrays) {
        return Arrays.stream(arrays)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }
}


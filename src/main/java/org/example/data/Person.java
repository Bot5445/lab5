package org.example.data;

import java.io.Serializable;

import java.util.Date;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.abs;

@Getter
public final class Person implements Serializable {

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
    private final Location location; //Поле не может быть null

    public Person(String name, Coordinates coordinates, Long height, Location location) {
        this.id = abs((new Random()).nextInt());
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.height = height;
        this.location = location;
    }

    public Person(Integer id, String name, Coordinates coordinates, Date creationDate, Long height, String passportID, Color hairColor, Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.height = height;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }


    public static String[] ToStrings(){
        return new String[]{"id", "name", "coordinates", };
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

        return id.toString() + "," + name + "," + coordinates.toString() + ","
                + creationDate.toString()+ ","+ hairColor + "," + nationality + "," + location.toString();
        // collectionManager
    }
}


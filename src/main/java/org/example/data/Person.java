package Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.random.RandomGenerator;
import lombok.Date;

@Getter
@Setter
public class Person {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate= new Date(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long height; //Поле не может быть null, Значение поля должно быть больше 0
    private String passportID; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле не может быть null

    public Person(String name, Coordinates coordinates, Long height, Location location) {
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.location = location;
    }

    public boolean setPassportID(String passportID) {
        this.passportID = passportID;
    }

}


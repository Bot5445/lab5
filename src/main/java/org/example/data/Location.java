package org.example.data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
public class Location {
    private final long x;
    private final Double y; //Поле не может быть null
    @Setter
    private String name; //Поле может быть null

    public Location(long x, Double y) {
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return x+","+y+","+name;
    }

}

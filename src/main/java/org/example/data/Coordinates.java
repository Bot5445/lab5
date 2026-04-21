package org.example.data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordinates {
    private float x;
    private float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //    @Override
//    public Map<String, Float> toStrings() {
//        return String.format("x: %f, y: %f", x, y);
//        return Map.of("x", x, "y", y);
        //TODO: fix it
//        return null;
//    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public static String[] toStrings() {
        String[] str= new String[2];
        str[0] = "coordinates: " + 'x';
        str[1] = "coordinates: " + 'y';
        return str;
    }
}

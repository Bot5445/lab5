package org.example.data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Coordinates {
    private float x;
    private float y;

//    @Override
    public Map<String, Float> toStrings() {
//        return String.format("x: %f, y: %f", x, y);
//        return Map.of("x", x, "y", y);
        //TODO: fix it
        return null;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}

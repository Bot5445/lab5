package IOStorage;

import java.util.Map;

public interface IStorage {
    void load(Map<String, String> x);
    void save(Map<String, String> x);
}

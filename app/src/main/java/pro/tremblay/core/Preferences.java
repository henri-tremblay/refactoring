package pro.tremblay.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Preferences {

    private static final Preferences INSTANCE = new Preferences();

    public static Preferences preferences() {
        return INSTANCE;
    }

    private final ConcurrentMap<String, String> preferences = new ConcurrentHashMap<>();

    private Preferences() {}

    public void put(String key, String value) {
        preferences.put(key, value);
    }

    public String getString(String key) {
        String value = preferences.get(key);
        if (value != null) {
            return value;
        }
        return System.getProperty(key);
    }

    public int getInteger(String key) {
        String value = getString(key);
        if (value == null) {
            throw new IllegalArgumentException(key + " is not a known preference");
        }
        return Integer.parseInt(value);
    }
}

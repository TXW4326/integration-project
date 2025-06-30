package aiss.githubminer.utils;

import java.util.LinkedHashMap;

// This class is a utility to build a LinkedHashMap with a fluent API style.
public final class LinkedHashMapBuilder extends LinkedHashMap<String, Object> {

    private LinkedHashMapBuilder() {}

    public static LinkedHashMapBuilder of() {
        return new LinkedHashMapBuilder();
    }

    public LinkedHashMapBuilder add (String key, Object value) {
        this.put(key, value);
        return this;
    }
}

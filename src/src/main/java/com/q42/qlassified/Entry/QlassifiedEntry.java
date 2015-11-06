package com.q42.qlassified.Entry;

public abstract class QlassifiedEntry<T> {

    public enum Type {
        BOOLEAN,
        FLOAT,
        INTEGER,
        LONG,
        STRING,
        SERIALIZABLE
    }

    protected final String key;
    protected final T value;

    public QlassifiedEntry(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public abstract String getKey();
    public abstract T getValue();
    public abstract Type getType();
}

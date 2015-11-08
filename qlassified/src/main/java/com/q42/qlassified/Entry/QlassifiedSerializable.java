package com.q42.qlassified.Entry;

import java.io.Serializable;

public class QlassifiedSerializable extends QlassifiedEntry<Serializable> {

    private final Type type = Type.SERIALIZABLE;

    public QlassifiedSerializable(String alias, Serializable value) {
        super(alias, String.valueOf(value));
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public Serializable getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return type;
    }
}

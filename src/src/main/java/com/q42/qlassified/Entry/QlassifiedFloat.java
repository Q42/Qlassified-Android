package com.q42.qlassified.Entry;

public class QlassifiedFloat extends QlassifiedEntry<Float> {

    private final Type type = Type.FLOAT;

    public QlassifiedFloat(String alias, Float value) {
        super(alias, value);
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public Float getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return type;
    }
}

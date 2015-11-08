package com.q42.qlassified.Entry;

public class QlassifiedInteger extends QlassifiedEntry<Integer> {

    private final Type type = Type.INTEGER;

    public QlassifiedInteger(String alias, Integer value) {
        super(alias, value);
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public Integer getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return type;
    }
}

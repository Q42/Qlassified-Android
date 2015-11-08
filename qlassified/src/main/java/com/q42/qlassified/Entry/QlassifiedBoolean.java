package com.q42.qlassified.Entry;

public class QlassifiedBoolean extends QlassifiedEntry<Boolean> {

    private final Type type = Type.BOOLEAN;

    public QlassifiedBoolean(String alias, Boolean value) {
        super(alias, value);
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public Boolean getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return type;
    }
}

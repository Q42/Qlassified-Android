package com.q42.qlassified.Entry;

public class QlassifiedString extends QlassifiedEntry<String> {

    private final Type type = Type.STRING;

    public QlassifiedString(String alias, String value) {
        super(alias, value);
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public String getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return type;
    }
}

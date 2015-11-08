package com.q42.qlassified.Entry;

public class QlassifiedLong extends QlassifiedEntry<Long> {

    private final Type type = Type.LONG;

    public QlassifiedLong(String alias, Long value) {
        super(alias, value);
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public Long getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return type;
    }
}

package com.comprame.library.rest;

public class Query {
    public final String string;

    public Query() {
        this(null);
    }

    Query(String string) {
        this.string = string;
    }

    public Query and(String key, Object value) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Query params cant be empty");
        }
        if (value == null)
            return this;
        if (string == null)
            return new Query(key + "=" + value);
        return new Query(string + "&" + key + "=" + value);
    }

    public static Query query(String key, Object value) {
        return new Query().and(key, value);
    }

}

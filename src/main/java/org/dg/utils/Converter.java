package org.dg.utils;

public abstract class Converter {

    protected <T> String toString(T obj) {
        return SerializeDeserialiseUtils.serializeObjectToJSON(obj);
    }

    protected static <T> T fromJsonString(String json, Class<T> clazz) {
        return SerializeDeserialiseUtils.deserializeJSONToObject(json, clazz);
    }

    public abstract String toString();

    protected abstract Object fromJsonString(String json);
}

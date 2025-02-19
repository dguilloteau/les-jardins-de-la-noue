package org.dg.utils;

import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializeDeserialiseUtils {

    SerializeDeserialiseUtils() {
    }

    /**
     * Serialise un objet en String au format json
     *
     * @param obj Objet à sérializer
     * @param <T> pour la générification de l'objet
     * @return String au format json
     */
    public static <T> String serializeObjectToJSON(T obj) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setExclusionStrategies(getExclusionStrategy())
                    .disableHtmlEscaping()
                    .create();
            return gson.toJson(obj);
        } catch (Exception e) {
            throw new ShopsException(ShopsErrors.INVALID_SERIALIZE_MESSAGE, obj.getClass().getSimpleName(),
                    e.getCause());
        }
    }

    /**
     * Désérialise un String en objet
     *
     * @param json  String à désérialiser
     * @param clazz de l'objet pour sa désérialisation
     * @return objet désérialisé
     */
    public static <T> T deserializeJSONToObject(String json, Class<T> clazz) {
        FormItemTypeAdapterFactory formItemTypeAdapterFactory = new FormItemTypeAdapterFactory();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(formItemTypeAdapterFactory)
                .setExclusionStrategies(getExclusionStrategy())
                .disableHtmlEscaping()
                .create();
        return gson.fromJson(json, clazz);
    }

    /**
     * Mettre  @ExcludeToGson pour exclure l'objet
     * @return
     */
    private static ExclusionStrategy getExclusionStrategy() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(ExcludeToGson.class) != null;
            }
        };
    }
}

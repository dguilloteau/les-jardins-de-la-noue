package org.dg.utils;

import java.io.IOException;

import org.dg.dto.FormItem;
import org.dg.dto.items.FImage;
import org.dg.dto.items.FList;
import org.dg.dto.items.FListElement;
import org.dg.dto.items.FQuestion;
import org.dg.dto.items.FText;
import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.quarkus.logging.Log;

public class FormItemTypeAdapterFactory implements TypeAdapterFactory {

    private static final String ID = "id";
    private static final String ITEM_ID = "itemId";
    private static final String NAME = "name";
    private static final String LIBELLE = "libelle";
    private static final String CHECKED = "checked";
    private static final String LISTE_CHOIX = "listeChoix";
    private static final String TEXTE = "texte";
    private static final String TYPE = "type";
    private static final String IMAGE_URI = "imageUri";
    private static final String TITRE = "titre";
    private static final String LIBELLE_CHOIX = "libelleChoix";

    private Object convert(JsonObject item) {
        switch (item.get(NAME).getAsString()) {
            case "IMAGE_BANDEAU", "LOCALISATION":
                return getFImage(item);
            case "E_MAIL", "NOM_PRENOM":
                return getFQuestion(item);
            case "PANIER", "CAGETTE", "FORMULE":
                return getFList(item);
            case "ENTETE", "COMPOSITION":
                return getFText(item);
            case "FProduct":
                // FProduct fProduct = (FProduct) formItem;
                // requests.add(buildProduct(formItem.getId(), fProduct, cpt));
                break;
            default:
                Log.debug("Erreur");
        }
        return null;
    }

    private static FImage getFImage(JsonObject jsonObject) {
        Log.debug("getFImage jsonObject = " + jsonObject);
        return new FImage(
                jsonObject.get(ID).getAsLong(),
                jsonObject.get(ITEM_ID).getAsString(), jsonObject.get(NAME).getAsString(),
                jsonObject.get(LIBELLE).getAsString(), jsonObject.get(CHECKED).getAsBoolean(),
                getAsString(jsonObject, TITRE),
                getAsString(jsonObject, IMAGE_URI));
    }

    private static FQuestion getFQuestion(JsonObject jsonObject) {
        Log.debug("getFQuestion jsonObject = " + jsonObject);
        return new FQuestion(
                jsonObject.get(ID).getAsLong(),
                jsonObject.get(ITEM_ID).getAsString(), jsonObject.get(NAME).getAsString(),
                jsonObject.get(LIBELLE).getAsString(), jsonObject.get(CHECKED).getAsBoolean(),
                getAsString(jsonObject, TITRE));
    }

    private static FList getFList(JsonObject jsonObject) {
        Log.debug("getFList jsonObject = " + jsonObject);
        FList fList = new FList(
                jsonObject.get(ID).getAsLong(),
                jsonObject.get(ITEM_ID).getAsString(), jsonObject.get(NAME).getAsString(),
                jsonObject.get(LIBELLE).getAsString(), jsonObject.get(CHECKED).getAsBoolean(),
                getAsString(jsonObject, TITRE),
                getAsString(jsonObject, TYPE));

        jsonObject.get(LISTE_CHOIX).getAsJsonArray().asList().stream()
                .forEach(jsonElement -> {
                    JsonObject jObj = jsonElement.getAsJsonObject();
                    fList.addFListToFListElement(new FListElement(
                            jObj.get(ID).getAsLong(),
                            getAsString(jObj, IMAGE_URI),
                            getAsString(jObj, LIBELLE_CHOIX)));
                });
        return fList;
    }

    private static FText getFText(JsonObject jsonObject) {
        Log.debug("getFText jsonObject = " + jsonObject);
        return new FText(
                jsonObject.get(ID).getAsLong(),
                jsonObject.get(ITEM_ID).getAsString(), jsonObject.get(NAME).getAsString(),
                jsonObject.get(LIBELLE).getAsString(), jsonObject.get(CHECKED).getAsBoolean(),
                getAsString(jsonObject, TITRE),
                getAsString(jsonObject, TEXTE));
    }

    private static String getAsString(JsonObject jsonObject, String search) {
        String element = null;
        JsonElement jsonElement = jsonObject.get(search);
        if (jsonElement != null) {
            return jsonElement.isJsonNull() ? null : jsonElement.getAsString();
        }
        return element;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!FormItem.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) {
                try {
                    delegate.write(out, value);
                } catch (IOException e) {
                    throw new ShopsException(ShopsErrors.INVALID_SERIALIZE_MESSAGE, "Erreur lors de write", e);
                }
            }

            @Override
            public T read(JsonReader jsonReader) {
                return (T) convert(JsonParser.parseReader(jsonReader).getAsJsonObject());
            }
        };
    }
}

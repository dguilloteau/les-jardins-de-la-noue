package org.dg.dto.items;

import java.util.List;

import org.dg.utils.SerializeDeserialiseUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FProduct {

    String titre;
    String imageUri;
    List<String> quantites;
    String typeSelection;

    public FProduct(String titre, String imageUri, List<String> quantites, String typeSelection) {
        this.titre = titre;
        this.imageUri = imageUri;
        this.quantites = quantites;
        this.typeSelection = typeSelection;
    }

    @Override
    public String toString() {
        return SerializeDeserialiseUtils.serializeObjectToJSON(this);
    }

}

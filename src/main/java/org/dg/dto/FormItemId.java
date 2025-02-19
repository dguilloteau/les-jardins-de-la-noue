package org.dg.dto;

import org.dg.dto.items.FImage;
import org.dg.dto.items.FList;
import org.dg.dto.items.FQuestion;
import org.dg.dto.items.FText;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormItemId {

    ENTETE("00000001", "ENTETE", "Entête", FText.class),
    IMAGE_BANDEAU("00000002", "IMAGE_BANDEAU", "Image bandeau", FImage.class),
    E_MAIL("00000003", "E_MAIL", "E-mail", FQuestion.class),
    NOM_PRENOM("00000004", "NOM_PRENOM", "Nom et Prénom", FQuestion.class),
    PANIER("00000005", "PANIER", "Quand récupérer son panier", FList.class),
    LOCALISATION("00000006", "LOCALISATION", "Localisation", FImage.class),
    CAGETTE("00000007", "CAGETTE", "Cagette ?", FList.class),
    FORMULE("00000008", "FORMULE", "Choix formule", FList.class),
    COMPOSITION("00000009", "COMPOSITION", "Composition panier", FText.class),
    AUTRE("00000009", "AUTRE", null, Object.class);

    private final String id;
    private final String name;
    private final String libelle;
    private final Class<?> clazz;

}

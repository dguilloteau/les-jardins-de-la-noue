package org.dg.db;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dg.dto.FormItem;
import org.dg.dto.FormItemId;
import org.dg.dto.TypeFormulaire;
import org.dg.dto.items.FImage;
import org.dg.dto.items.FList;
import org.dg.dto.items.FListElement;
import org.dg.dto.items.FQuestion;
import org.dg.dto.items.FText;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TypesFormulairesInit {

    private static final String LIBELLE_PANIER_A_COMPOSER = "Panier à composer vous même \"Voir volet déroulant ci-dessous\" (Prix en fonction de ce que vous y mettez)";
    private static final String IMAGE_PANIER_A_COMPOSER = "https://lh6.googleusercontent.com/54eiiL_kprbNgxyUS416hUnBVUoTlc1j24Ys82adoBcGhTlMvNuzmhn7kMoZextVL5nJHonmGKFppApONL4ujiMMpO4SC8y4LuzyFk8MpHm7SFpf2yJteb1Vx3HUDqqNMuVMFQ6-7_M=w260";
    private static final String LIBELLE_GRAND_PANIER = "Grand panier composé par nos soins (20€)";
    private static final String IMAGE_GRAND_PANIER = "https://lh6.googleusercontent.com/B3w8QADXoOm40-cE1XrEnfYLAHWpQdC755QjYOM5jvjz0fWow-vSU7s1Ln-J515fLD8ntM4Takc6M9nlxLTk5vicZ78tEf4Xw3DvAF6GVDWn8VY5XDWo6XKp8eLC9t_QpYUomtYHxCo=w256";
    private static final String LIBELLE_PETIT_PANIER = "Petit panier composé par nos soins (10€)";
    private static final String IMAGE_PETIT_PANIER = "https://lh3.googleusercontent.com/rRCWxBdLlhn3veT2mva3lGichskG4y3LzmLCfLtWqaEEGdE42ldQQtscfAy3luJ8GxvGDn9M7g4P6JSlBKwr6xwP4XoB5WKVG1dLeynRrHhB3ZtZYx2VA0YzcVbtY2_ZC9ErN52_Fv8=w260";
    private static final String J_AI_DE_QUOI_RECUPERER_MES_LEGUMES = "J'ai de quoi récupérer mes légumes";
    private static final String JE_SOUHAITE_UNE_CAGETTE = "Je souhaite une cagette (1€)";
    private static final String SAMEDI = "Samedi sur le marché de Prahecq de 8h30 à 12h30 (commande avant jeudi soir)";
    private static final String VENDREDI = "Vendredi à la ferme de 16h à 19h (commande avant mercredi soir)";
    private static final String JEUDI = "Jeudi à la ferme de 16h à 19h (commande avant mardi soir)";
    private static final String MERCREDI_CHIZE = "Mercredi sur le marché de Chizé de 17h à 20h (commande avant lundi soir)";
    private static final String MERCREDI_BEAUVOIR = "Mercredi sur le marché de Beauvoir de 8h30 à 12h30 (commande avant lundi soir)";
    private static final String MARDI = "Mardi à la ferme de 16h à 19h (commande avant dimanche soir)";
    private static final String LUNDI = "Lundi à la ferme de 16h à 19h (commande avant dimanche soir)";
    private static final String DPGD_TYPE = "DPGD";
    private static final String CNRS_TYPE = "CNRS";
    private static final String FERME_TYPE = "Ferme";
    private static final String FORM_DESCRIPTION = """
            Bonjour,

            Nous vous proposons via ce formulaire de commander votre panier 48h avant le jour choisi de réception.

            Notre moyen de paiement : chèque ou espèce

            Pour toutes questions, n'hésitez pas à nous contacter :
            Tel : 0659557853
            mail : lesjardinsdelanoue@gmail.com

            A bientôt,

            Les jardins de la Noue""";

    private static final String BANDEAU_IMAGE_URI = "https://lh4.googleusercontent.com/kZpTkJvzRJnIq-L3NhboXBcvOYGdfoFiCzUDJhstt-jHOlWILPJmxBkMtLHJ4TZ8tyCYFWOW0yfrfDJFJCuDthgFk94zwDVQSSQsC9yRkt7Enk2Agu1yyfvU3rF0FiSDI2a2g64UBI4=w1057";

    private static final String ADRESSE_MAIL_TITLE = "Adresse e-mail";

    private static final String NOM_PRENOM_TITLE = "Nom et Prénom";

    private static final String GET_PANIER_TITLE_LIST = "Quel jour souhaitez vous récupérer votre panier ?";
    private static final String GET_PANIER_TYPE_LIST = "RADIO";

    private static final String LOCALISATION_TITLE = "Localisation de la ferme (Adresse: Chemin de la Noue, 79360 Marigny)";
    private static final String LOCALISATION_IMAGE_URI = "https://lh4.googleusercontent.com/2Q3-G_BFMFUPFUb6Eb1dInRas1inWG3mOSieLKBm0M4U1qkr_PQLLAS8VaXN0awKZU9wNGQsXxULyFcnuaoS1wy70sZYjN30FS2P8oT0p5u7v3dDSdKzKJ3UtXYwMQPEGal0XkORKbE=w420";

    private static final String CAGETTE_TITLE_LIST = "Souhaitez vous une cagette pour récupérer vos légumes ?";
    private static final String CAGETTE_TYPE_LIST = "RADIO";

    private static final String FORMULE_TITLE_LIST = "Quelle formule souhaitez vous ?";
    private static final String FORMULE_TYPE_LIST = "CHECKBOX";

    private static final String COMPOSE_PANIER_TEXT = """
            Composez votre panier vous même en choisissant les quantités via les menus déroulant.

            Vous pouvez rajouter à votre panier (composé par nos soins) les légumes que l'on propose ci-dessous, directement lors de votre venue à la ferme ou sur les marchés.""";

    /*
     * private static final String PRODUIT_TYPE_SELECTION = "DROP_DOWN";
     * private static final String PRODUIT_TITRE = "Radis Noir - 3,80€/Kg";
     * private static final String PRODUIT_IMAGE_URI =
     * "https://lh5.googleusercontent.com/ZBCrDWkzInpqPi4OmsdCgHIe1IoATF8u8RtRGNch32UoaG8v1NsmGHkyXsFPwJJKnDYLgCB0hDGz9jsMDavHVuhBClr8lJMfyrHKbOGnyCOHSJkifCOwp5QO3XPlVV3jHajJI1S7HpM=w200";
     * private static final List<String> PRODUITS_QUANTITES = Arrays.asList(
     * "0.250 Kg", "0.500 Kg", "0.750 Kg", "1.000 Kg", "1.500 Kg", "2.000 Kg",
     * "3.000 Kg", "4.000 Kg",
     * "5.000 Kg");
     */

    TypesFormulairesInit() {
    }

    public static List<TypeFormulaire> initTypesFormulaires() {
        return Arrays.asList(
                initTypeFormulaireFerme(),
                initTypeFormulaireCNRS(),
                initTypeFormulaireDPGD());
    }

    private static String getDefaultTitre(String type) {
        LocalDate date = LocalDate.now();
        String numSemaine = String.valueOf(date.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        if (FERME_TYPE.equals(type)) {
            return "Panier de légumes - Semaine " + numSemaine + " - Les jardins de la noue";
        } else {
            return "Panier de légumes " + type + " - Semaine " + numSemaine + " - Les jardins de la noue";
        }
    }

    private static TypeFormulaire initTypeFormulaireFerme() {
        List<FormItem> formItems = new ArrayList<>();

        formItems.add(new FText(FormItemId.ENTETE, true, getDefaultTitre(FERME_TYPE), FORM_DESCRIPTION));

        formItems.add(new FImage(FormItemId.IMAGE_BANDEAU, true, null, BANDEAU_IMAGE_URI));

        formItems.add(new FQuestion(FormItemId.E_MAIL, true, ADRESSE_MAIL_TITLE));

        formItems.add(new FQuestion(FormItemId.NOM_PRENOM, true, NOM_PRENOM_TITLE));

        formItems.add(new FList(FormItemId.PANIER, true, GET_PANIER_TITLE_LIST, GET_PANIER_TYPE_LIST)
                .addFListToFListElement(new FListElement(null, LUNDI))
                .addFListToFListElement(new FListElement(null, MARDI))
                .addFListToFListElement(new FListElement(null, MERCREDI_BEAUVOIR))
                .addFListToFListElement(new FListElement(null, MERCREDI_CHIZE))
                .addFListToFListElement(new FListElement(null, JEUDI))
                .addFListToFListElement(new FListElement(null, VENDREDI))
                .addFListToFListElement(new FListElement(null, SAMEDI)));

        formItems.add(new FImage(FormItemId.LOCALISATION, true, LOCALISATION_TITLE, LOCALISATION_IMAGE_URI));

        formItems.add(new FList(FormItemId.CAGETTE, true, CAGETTE_TITLE_LIST, CAGETTE_TYPE_LIST)
                .addFListToFListElement(new FListElement(null, JE_SOUHAITE_UNE_CAGETTE))
                .addFListToFListElement(new FListElement(null, J_AI_DE_QUOI_RECUPERER_MES_LEGUMES)));

        formItems.add(new FList(FormItemId.FORMULE, true, FORMULE_TITLE_LIST, FORMULE_TYPE_LIST)
                .addFListToFListElement(new FListElement(IMAGE_PETIT_PANIER, LIBELLE_PETIT_PANIER))
                .addFListToFListElement(new FListElement(IMAGE_GRAND_PANIER, LIBELLE_GRAND_PANIER))
                .addFListToFListElement(new FListElement(IMAGE_PANIER_A_COMPOSER, LIBELLE_PANIER_A_COMPOSER)));

        formItems.add(new FText(FormItemId.COMPOSITION, true, null, COMPOSE_PANIER_TEXT));

        return new TypeFormulaire(FERME_TYPE, formItems);
    }

    private static TypeFormulaire initTypeFormulaireCNRS() {
        List<FormItem> formItems = new ArrayList<>();
        formItems.add(new FText(FormItemId.ENTETE, true, getDefaultTitre(CNRS_TYPE), FORM_DESCRIPTION));

        formItems.add(new FImage(FormItemId.IMAGE_BANDEAU, true, null, BANDEAU_IMAGE_URI));

        formItems.add(new FQuestion(FormItemId.E_MAIL, true, ADRESSE_MAIL_TITLE));

        formItems.add(new FQuestion(FormItemId.NOM_PRENOM, false, NOM_PRENOM_TITLE));

        formItems.add(new FList(FormItemId.PANIER, false, GET_PANIER_TITLE_LIST, GET_PANIER_TYPE_LIST)
                .addFListToFListElement(new FListElement(null, LUNDI))
                .addFListToFListElement(new FListElement(null, MARDI))
                .addFListToFListElement(new FListElement(null, MERCREDI_BEAUVOIR))
                .addFListToFListElement(new FListElement(null, MERCREDI_CHIZE))
                .addFListToFListElement(new FListElement(null, JEUDI))
                .addFListToFListElement(new FListElement(null, VENDREDI))
                .addFListToFListElement(new FListElement(null, SAMEDI)));

        formItems.add(new FImage(FormItemId.LOCALISATION, false, LOCALISATION_TITLE, LOCALISATION_IMAGE_URI));

        formItems.add(new FList(FormItemId.CAGETTE, false, CAGETTE_TITLE_LIST, CAGETTE_TYPE_LIST)
                .addFListToFListElement(new FListElement(null, JE_SOUHAITE_UNE_CAGETTE))
                .addFListToFListElement(new FListElement(null, J_AI_DE_QUOI_RECUPERER_MES_LEGUMES)));

        formItems.add(new FList(FormItemId.FORMULE, false, FORMULE_TITLE_LIST, FORMULE_TYPE_LIST)
                .addFListToFListElement(new FListElement(IMAGE_PETIT_PANIER, LIBELLE_PETIT_PANIER))
                .addFListToFListElement(new FListElement(IMAGE_GRAND_PANIER, LIBELLE_GRAND_PANIER))
                .addFListToFListElement(new FListElement(IMAGE_PANIER_A_COMPOSER, LIBELLE_PANIER_A_COMPOSER)));

        formItems.add(new FText(FormItemId.COMPOSITION, false, null, COMPOSE_PANIER_TEXT));

        return new TypeFormulaire(CNRS_TYPE, formItems);
    }

    private static TypeFormulaire initTypeFormulaireDPGD() {
        List<FormItem> formItems = new ArrayList<>();
        formItems.add(new FText(FormItemId.ENTETE, true, getDefaultTitre(DPGD_TYPE), FORM_DESCRIPTION));

        formItems.add(new FImage(FormItemId.IMAGE_BANDEAU, true, null, BANDEAU_IMAGE_URI));

        formItems.add(new FQuestion(FormItemId.E_MAIL, true, ADRESSE_MAIL_TITLE));

        formItems.add(new FQuestion(FormItemId.NOM_PRENOM, false, NOM_PRENOM_TITLE));

        formItems.add(new FList(FormItemId.PANIER, false, GET_PANIER_TITLE_LIST, GET_PANIER_TYPE_LIST)
                .addFListToFListElement(new FListElement(null, LUNDI))
                .addFListToFListElement(new FListElement(null, MARDI))
                .addFListToFListElement(new FListElement(null, MERCREDI_BEAUVOIR))
                .addFListToFListElement(new FListElement(null, MERCREDI_CHIZE))
                .addFListToFListElement(new FListElement(null, JEUDI))
                .addFListToFListElement(new FListElement(null, VENDREDI))
                .addFListToFListElement(new FListElement(null, SAMEDI)));

        formItems.add(new FImage(FormItemId.LOCALISATION, false, LOCALISATION_TITLE, LOCALISATION_IMAGE_URI));

        formItems.add(new FList(FormItemId.CAGETTE, false, CAGETTE_TITLE_LIST, CAGETTE_TYPE_LIST)
                .addFListToFListElement(new FListElement(null, JE_SOUHAITE_UNE_CAGETTE))
                .addFListToFListElement(new FListElement(null, J_AI_DE_QUOI_RECUPERER_MES_LEGUMES)));

        formItems.add(new FList(FormItemId.FORMULE, false, FORMULE_TITLE_LIST, FORMULE_TYPE_LIST)
                .addFListToFListElement(new FListElement(IMAGE_PETIT_PANIER, LIBELLE_PETIT_PANIER))
                .addFListToFListElement(new FListElement(IMAGE_GRAND_PANIER, LIBELLE_GRAND_PANIER))
                .addFListToFListElement(new FListElement(IMAGE_PANIER_A_COMPOSER, LIBELLE_PANIER_A_COMPOSER)));

        formItems.add(new FText(FormItemId.COMPOSITION, false, null, COMPOSE_PANIER_TEXT));

        return new TypeFormulaire(DPGD_TYPE, formItems);
    }
}

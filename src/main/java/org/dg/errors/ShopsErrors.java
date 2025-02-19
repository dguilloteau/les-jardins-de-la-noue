package org.dg.errors;

import jakarta.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopsErrors {

    /* Errors list */
    GENERATION_ERROR(Status.INTERNAL_SERVER_ERROR, "Erreur lors de la génération du formulaire"),
    INVALID_SERIALIZE_MESSAGE(Status.INTERNAL_SERVER_ERROR, "Impossible de sérialiser le message"),
    INVALID_DESERIALIZE_MESSAGE(Status.INTERNAL_SERVER_ERROR, "Impossible de desérialiser le message"),
    INVALID_PUBLISH_MESSAGE(Status.BAD_REQUEST, "Le formulaire n'a pas été publié"),
    SQL_ERROR(Status.BAD_REQUEST, "Une erreur SQL est arrivée"),
    FORMULAIRE_NOT_FOUND(Status.NOT_FOUND, "Aucun formulaire n'a été trouvé"),
    RESPONSE_ERROR(Status.INTERNAL_SERVER_ERROR, "Problème lors de la récupération des réponses"),
    CREDENTIAL_ERROR(Status.INTERNAL_SERVER_ERROR, "Problème lors de la récupération des credentials"),
    ;
    /* END errors list */

    private final Status status;
    private final String description;

}

package org.dg.utils;

import java.util.Optional;

import org.dg.dto.Client;
import org.dg.dto.TypeFormulaire;

public class ClientsUtils {

    ClientsUtils() {
    }

    /**
     * Initialise l'id des clients à null pour le type de formulaire
     * @param typeFormulaire
     */
    public static void initClientsOfTypeFormulaire(TypeFormulaire typeFormulaire) {
        typeFormulaire.getClients().forEach(client -> client.setId(null));
    }

    /**
     * Trouve le client dans le type de formulaire en fonction de son email
     * 
     * @param typeFormulaire
     * @param email
     * @return Le client si présent
     */
    public static Optional<Client> getClientOfTypeFormulaire(TypeFormulaire typeFormulaire, String email) {
        return typeFormulaire.getClients().stream().filter(cli -> cli.getEmail().equals(email)).findFirst();
    }
}

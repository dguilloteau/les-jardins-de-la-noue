package org.dg.services;

import java.util.Optional;

import org.dg.dto.Client;
import org.dg.dto.TypeFormulaire;
import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;
import org.dg.repositories.ClientRepository;
import org.dg.repositories.TypeFormulaireRepository;
import org.dg.utils.ClientsUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClientsService {

    private final ClientRepository clientRepository;
    private final TypesFormulairesService typesFormulairesService;
    private final TypeFormulaireRepository typeFormulaireRepository;

    @Inject
    public ClientsService(
            TypesFormulairesService typesFormulairesService,
            ClientRepository clientRepository,
            TypeFormulaireRepository typeFormulaireRepository) {
        this.typesFormulairesService = typesFormulairesService;
        this.clientRepository = clientRepository;
        this.typeFormulaireRepository = typeFormulaireRepository;
    }

    public Client postClientType(Long id, String body) {
        // Construction du client
        Client client = new Client().fromJsonString(body);
        client.setId(null);
        // Récupération du TypeFormulaire concerné
        TypeFormulaire typeFormulaire = typesFormulairesService.getTypeFormulaire(id);

        // Récupération du client dans le type de formulaire
        Optional<Client> optClient = ClientsUtils.getClientOfTypeFormulaire(typeFormulaire, client.getEmail());

        // Si le client est déjà présent, alors, on ne le rajoute pas
        if (optClient.isPresent()) {
            throw new ShopsException(ShopsErrors.SQL_ERROR, "Le client existe déjà pour ce type de formulaire");
        }
        typeFormulaire.getClients().add(client);
        typeFormulaire = typeFormulaireRepository.save(typeFormulaire);
        optClient = ClientsUtils.getClientOfTypeFormulaire(typeFormulaire, client.getEmail());
        if (optClient.isEmpty()) {
            throw new ShopsException(ShopsErrors.SQL_ERROR,
                    "Le client n'a pas pu être sauvegardé pour ce type de formulaire");
        } else {
            return optClient.get();
        }
    }

    /**
     * Supprime de client dans le type de formulaire
     * l'id du client est différent si il est dans un autre type de formulaire
     * 
     * @param idType
     * @param idClient
     */
    public void deleteClientType(Long idClient) {
        clientRepository.deleteById(idClient);
    }

    /**
     * Modification d'un client
     * @param body
     */
    public void patchClientType(String body) {
        // Construction du client
        Client client = new Client().fromJsonString(body);
        clientRepository.save(client);
    }
}

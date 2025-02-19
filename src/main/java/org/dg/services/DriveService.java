package org.dg.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dg.dto.Formulaire;
import org.dg.dto.TypeFormulaire;
import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DriveService {

    private static final String READER_ROLE = "reader";
    CredentialService credentialService;

    @Inject
    public DriveService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    /**
     * Return true si le fomulaire a bien été publié
     * 
     * @param formId du formulaire
     * @return true or false
     */
    public boolean publishForm(String formId) {
        PermissionList list;
        try {
            list = credentialService.getDriveService().permissions().list(formId).execute();

            if (list.getPermissions().stream().filter(it -> it.getRole().equals(READER_ROLE)).findAny().isEmpty()) {
                Permission body = new Permission();
                body.setRole(READER_ROLE);
                body.setType("anyone");
                credentialService.getDriveService().permissions().create(formId, body).execute();
                return true;
            }
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.INVALID_PUBLISH_MESSAGE,
                    "Erreur lors de la récupération des permissions", e);
        }

        return false;
    }

    /**
     * Retourne une liste de "pageSize" formulaires actifs
     * 
     * @param pageSize nombre de formulaires à analyser
     * @return FormulairesActifs (pas trashed) dans les pageSize
     */
    public List<Formulaire> getActivesForms(Integer pageSize) {
        // Print the names and IDs for up to 10 files.
        List<Formulaire> formulaires = new ArrayList<>();
        String pageToken = null;
        do {
            FileList result;
            try {
                result = credentialService.getDriveService().files().list()
                        .setSpaces("drive") // dans le drive
                        .setQ("mimeType='application/vnd.google-apps.form' and trashed=false") // tous les formulaires
                                                                                               // pas à
                                                                                               // la poubelle
                        .setOrderBy("modifiedTime desc") // modifié le plus récemment
                        .setPageSize(pageSize)
                        .setFields("nextPageToken,files(id,name,modifiedTime,labelInfo)")
                        .setPageToken(pageToken)
                        .execute();
            } catch (IOException e) {
                throw new ShopsException(ShopsErrors.FORMULAIRE_NOT_FOUND, "Erreur lors de getActivesForms", e);
            }
            List<File> files = result.getFiles();

            if (files == null || files.isEmpty()) {
                Log.debug("No files found.");
            } else {
                for (File file : files) {
                    Formulaire formulaire = new Formulaire(file.getId(), new TypeFormulaire(),
                            file.getModifiedTime().toString());
                    formulaires.add(formulaire);
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return formulaires;
    }

}

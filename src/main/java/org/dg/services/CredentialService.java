package org.dg.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.FormsScopes;

import jakarta.enterprise.context.ApplicationScoped;

// https://developers.google.com/identity/oauth2/web/guides/get-google-api-clientid?hl=fr
// activer google drive api

@ApplicationScoped
public class CredentialService {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Les jardins de la noue";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE, FormsScopes.FORMS_BODY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Drive driveService;
    private Forms formsService;

    public CredentialService() {
        // Build a new authorized API client service.
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentials(httpTransport);
            this.driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            this.formsService = new Forms.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new ShopsException(ShopsErrors.CREDENTIAL_ERROR, "Erreur lors de CredentialService", e);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     */
    private Credential getCredentials(final NetHttpTransport httpTransport) {
        // Load client secrets.
        try (InputStream in = CredentialService.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            if (in == null) {
                throw new ShopsException(ShopsErrors.CREDENTIAL_ERROR, "Resource not found: " + CREDENTIALS_FILE_PATH);
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            // returns an authorized Credential object.
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            throw new ShopsException(ShopsErrors.CREDENTIAL_ERROR, "Erreur lors de getCredentials", e);
        }
    }

    public Drive getDriveService() {
        return driveService;
    }

    public Forms getFormsService() {
        return formsService;
    }
}

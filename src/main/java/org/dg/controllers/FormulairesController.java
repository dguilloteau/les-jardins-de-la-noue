package org.dg.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dg.dto.Formulaire;
import org.dg.errors.ShopsException;
import org.dg.services.DriveService;
import org.dg.services.FormulairesService;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/formulaire")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FormulairesController {

    private FormulairesService formulairesService;
    private DriveService driveService;

    @Inject
    public FormulairesController(FormulairesService formulairesService, DriveService driveService) {
        this.formulairesService = formulairesService;
        this.driveService = driveService;
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(ShopsException e) {
        return RestResponse.status(e.error.getStatus(), e.error.getDescription());
    }

    @GET
    @Path("/all")
    @Transactional
    public RestResponse<String> getAllForms() {
        Log.info("getAllForms");
        // Récupération des formulaires actifs
        List<Formulaire> formulaires = driveService.getActivesForms(10);
        return ResponseBuilder.ok(formulairesService.getFormulaires(formulaires).toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("/create")
    @Transactional
    public RestResponse<String> postFormulaire(String body) {
        Log.info("postFormulaire = " + body);
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter("postFormulaireDPGD.json"))) {
        //     writer.write(body);
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // Création et persist en base
        Formulaire formulaire = formulairesService.createNewForm(new Formulaire().fromJsonString(body));
        Log.info("formulaireCree = " + formulaire);
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter("postFormulaireFermeResponse.json"))) {
        //     writer.write(formulaire.toString());
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        return ResponseBuilder.ok(formulaire.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{formId}")
    public RestResponse<String> getForm(@PathParam("formId") String formId) {
        Log.info("getForm = " + formId);
        String res = formulairesService.getFormAsPrettyString(formId);
        Log.info("res = " + res);
        return ResponseBuilder
                .ok(res, MediaType.APPLICATION_JSON)
                .build();
    }

}

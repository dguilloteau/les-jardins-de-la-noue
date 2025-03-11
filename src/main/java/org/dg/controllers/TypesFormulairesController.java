package org.dg.controllers;

import org.dg.errors.ShopsException;
import org.dg.services.TypesFormulairesService;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/formulaire/types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypesFormulairesController {

    private final TypesFormulairesService typesFormulairesService;

    @ServerExceptionMapper
    public RestResponse<String> mapException(ShopsException e) {
        return RestResponse.status(e.getError().getStatus(), e.getError().getDescription() + " => " + e.getMessage());
    }

    @Inject
    public TypesFormulairesController(TypesFormulairesService typesFormulairesService) {
        this.typesFormulairesService = typesFormulairesService;
    }

    @GET
    @Path("/all")
    @Transactional
    public RestResponse<String> getAllFormsTypes() {
        Log.info("getAllFormsTypes");
        String jsonResponse = typesFormulairesService.getAllDefaultTypeFormulaire().toString();
        Log.debug("jsonResponse = " + jsonResponse);

        return ResponseBuilder.ok(jsonResponse, MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public RestResponse<String> getFormType(@PathParam("id") Long id) {
        Log.info("getFormType id = " + id);
        String jsonResponse = typesFormulairesService.getTypeFormulaire(id).toString();
        Log.debug("jsonResponse = " + jsonResponse);

        return ResponseBuilder.ok(jsonResponse, MediaType.APPLICATION_JSON)
                .build();
    }

    @PATCH
    @Path("/formItem")
    @Transactional
    public RestResponse<String> patchFormItemType(String body) {
        Log.info("patchFormItemType");
        Log.info("body = " + body);
        typesFormulairesService.patchFormItemType(body);
        return ResponseBuilder.ok("OK", MediaType.APPLICATION_JSON)
                .build();
    }

}

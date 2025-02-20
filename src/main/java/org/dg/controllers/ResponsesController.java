package org.dg.controllers;

import org.dg.errors.ShopsException;
import org.dg.services.FormulairesService;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/formulaire/responses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResponsesController {

    private final FormulairesService formulairesService;

    @Inject
    public ResponsesController(FormulairesService formulairesService) {
        this.formulairesService = formulairesService;
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(ShopsException e) {
        return RestResponse.status(e.getError().getStatus(), e.getError().getDescription());
    }

    @GET
    @Path("/{formId}")
    public RestResponse<String> getResponses(@PathParam("formId") String formId) {
        return ResponseBuilder.ok(formulairesService.getResponses(formId), MediaType.APPLICATION_JSON).build();
    }

    @PATCH
    @Path("/{formId}")
    public RestResponse<String> desactiveFormResponse(@PathParam("formId") String formId) {
        return ResponseBuilder.ok(formulairesService.desactiveFormResponse(formId), MediaType.APPLICATION_JSON)
                .build();
    }
}

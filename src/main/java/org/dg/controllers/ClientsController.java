package org.dg.controllers;

import org.dg.dto.Client;
import org.dg.errors.ShopsException;
import org.dg.services.ClientsService;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/formulaire/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientsController {

    private final ClientsService clientsService;

    @ServerExceptionMapper
    public RestResponse<String> mapException(ShopsException e) {
        return RestResponse.status(e.getError().getStatus(), e.getError().getDescription() + " => " + e.getMessage());
    }

    @Inject
    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @POST
    @Path("/ofType/{idType}")
    @Transactional
    public RestResponse<String> postClientType(@PathParam("idType") Long id, String body) {
        Log.info("postClientType for idType = " + id);
        Log.info("body = " + body);
        Client client = clientsService.postClientType(id, body);
        Log.info("clientCree = " + client);
        return ResponseBuilder.ok(client.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    @DELETE
    @Path("/client/{idClient}")
    @Transactional
    public RestResponse<String> deleteClientType(@PathParam("idClient") Long idClient) {
        Log.info("deleteClientType " + idClient);
        clientsService.deleteClientType(idClient);
        return ResponseBuilder.ok("OK", MediaType.APPLICATION_JSON)
                .build();
    }

    @PATCH
    @Path("/client")
    @Transactional
    public RestResponse<String> patchClientType(String body) {
        Log.info("patchClientType");
        Log.info("body = " + body);
        clientsService.patchClientType(body);
        return ResponseBuilder.ok("OK", MediaType.APPLICATION_JSON)
                .build();
    }
}

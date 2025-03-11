package org.dg.controllers;

import org.dg.services.MailService;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// https://sedona.fr/2022/03/17/comment-envoyer-des-emails-responsive-avec-quarkus-et-mjml/
// build docker-compose.yml
// http://localhost:8025/  --> MailHog
@Path("/formulaire/mail")
public class MailController {

    private final MailService mailService;

    @Inject
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GET
    @Path("/send/{formId}")
    @Transactional
    public Response sendEmailWithMailTemplate(@PathParam("formId") String formId) {
        mailService.sendTypeSafeMailTemplate("elamotte7@test.com",
                new String[] { "elamotte7@mail.com", "elamotte7@test.fr" },
                "subject",
                new String[] { "elamotte@cc.fr" }, formId);

        return Response.ok().build();
    }

    @GET
    @Path("/render/{formId}")
    @Transactional
    @Produces(MediaType.TEXT_HTML)
    public RestResponse<String> getMailRender(@PathParam("formId") String formId) {
        Log.info("getMailRender = " + formId);
        String htmlPage = mailService.getMailTemplateInstance(formId).templateInstance().render();
        // On ne retourne que l'iframe
        String res = htmlPage.substring(htmlPage.lastIndexOf("<iframe"), htmlPage.lastIndexOf("</iframe>") + 9);
        Log.info("res = " + res);
        return ResponseBuilder.ok(res, MediaType.TEXT_HTML).build();
    }
}

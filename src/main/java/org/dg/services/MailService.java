package org.dg.services;

import java.util.Arrays;
import java.util.Optional;

import org.dg.dto.FormItem;
import org.dg.dto.FormItemId;
import org.dg.dto.Formulaire;
import org.dg.dto.email.EmailNotification;
import org.dg.dto.items.FText;
import org.dg.repositories.FormulaireRepository;
import org.dg.template.EmailTemplates;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MailService {

    private final Mailer mailer;
    private final FormulaireRepository formulaireRepository;

    @Inject
    public MailService(Mailer mailer, FormulaireRepository formulaireRepository) {
        this.mailer = mailer;
        this.formulaireRepository = formulaireRepository;

    }

    /**
     * Send html mail using parameter in sync mode with Template
     *
     * @param from     the email sender
     * @param toEmails the email recipients
     * @param subject  the email subject
     * @param ccEmails the hidden email recipients
     * @param template the html email template
     */
    public void sendTypeSafeTemplate(String from,
            String[] toEmails,
            String subject,
            String[] ccEmails,
            TemplateInstance template) {
        Log.info("Sending email to " + String.join(", ", toEmails) + " from " + from);
        mailer.send(Mail.withHtml(
                " ",
                subject,
                template.render())
                .setFrom(from)
                .setTo(Arrays.stream(toEmails).toList())
                .setCc(Arrays.stream(ccEmails).toList()));
    }

    private EmailNotification getEmailNotification(String formId) {
        Formulaire formulaire = formulaireRepository.getFormulaireByFormId(formId);
        Log.info("formulaire = " + formulaire);
        Optional<FormItem> formItem = formulaire.getTypeFormulaire().getFormItem(FormItemId.ENTETE);
        if (formItem.isPresent()) {
            FText text = (FText) formItem.get();
            return EmailNotification.builder()
                    .titre(text.getTitre())
                    .responderUri(formulaire.getResponderUri())
                    .build();
        }
        return null;
    }

    public MailTemplateInstance getMailTemplateInstance(String formId) {
        return EmailTemplates.Templates.emailNotificationMailTemplate(getEmailNotification(formId));
    }

    /**
     * Send html mail using parameter in async mode with MailTemplate
     *
     * @param from     the email sender
     * @param toEmails the email recipients
     * @param subject  the email subject
     * @param ccEmails the hidden email recipients
     * @param template the html email template
     */
    public void sendTypeSafeMailTemplate(String from,
            String[] toEmails,
            String subject,
            String[] ccEmails,
            String formId) {
        MailTemplateInstance template = getMailTemplateInstance(formId);
        Log.info("Sending email to " + String.join(", ", toEmails) + " from " + from);
        template
                .from(from)
                .to(toEmails)
                .subject(subject)
                .cc(ccEmails)
                .send().subscribe().with(
                        it -> Log.info("Email sent"),
                        error -> Log.error("A problem occured when sending email", error));
    }


}

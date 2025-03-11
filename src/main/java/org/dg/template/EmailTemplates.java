package org.dg.template;

import org.dg.dto.email.EmailNotification;

import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.qute.CheckedTemplate;

public class EmailTemplates {

  EmailTemplates() {
  }

  @CheckedTemplate
  public static class Templates {
    Templates() {
    }

    public static native MailTemplateInstance emailNotificationMailTemplate(EmailNotification context);
  }
}

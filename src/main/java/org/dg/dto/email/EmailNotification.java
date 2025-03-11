package org.dg.dto.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmailNotification {

    private String responderUri;
    private String titre;
}
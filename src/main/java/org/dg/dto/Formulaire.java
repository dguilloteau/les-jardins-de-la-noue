package org.dg.dto;

import org.dg.dto.items.FList;
import org.dg.utils.Converter;

import com.google.api.client.util.DateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Formulaire extends Converter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Id du formulaire (celui du drive)
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String formId;

    @OneToOne(targetEntity = TypeFormulaire.class, cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_Formulaire_TypeFormulaire", value = ConstraintMode.CONSTRAINT), name = "typeFormulaire_id", referencedColumnName = "id", unique = true)
    private TypeFormulaire typeFormulaire;

    // Date de la dernière modification du formulaire
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String dateDerniereModif;

    // Le formulaire est créé
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private boolean created;

    // TODO voir ça
    // Le formulaire est terminé
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private boolean done;

    public Formulaire(String formId, TypeFormulaire typeFormulaire, String dateDerniereModif) {
        this.formId = formId;
        this.typeFormulaire = typeFormulaire;
        this.dateDerniereModif = dateDerniereModif;
        this.created = true;
        this.done = false; // FIXME à renseigner
    }

    /**
     * Il faut remettre l'id à null pour qu'il soit regénéré lors de
     * l'enregistrement
     */
    public void initNewFormulaire() {
        // Id de TypeFormulaire
        this.getTypeFormulaire().setId(null);
        this.getTypeFormulaire().getFormItems().forEach(item -> {
            if (FList.class.isAssignableFrom(item.getClass())) {
                // Id de FListElement
                ((FList) item).getListeChoix().stream().forEach(choix -> choix.setId(null));
            }
            // Id de FormItem
            item.setId(null);
        });
        // Le type de formulaire enregistré n'est pas un défaut
        this.getTypeFormulaire().setDefaut(false);
        // Ajout de la date de dernière modif qui est now
        this.setDateDerniereModif(new DateTime(System.currentTimeMillis()).toString());
        // Le formulaire est créé
        this.setCreated(true);
    }

    @Override
    public String toString() {
        return super.toString(this);
    }

    @Override
    public Formulaire fromJsonString(String json) {
        return Converter.fromJsonString(json, Formulaire.class);
    }

}

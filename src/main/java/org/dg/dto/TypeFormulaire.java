package org.dg.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dg.utils.SerializeDeserialiseUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TypeFormulaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // C'est un type de formulaire par defaut
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private boolean defaut;

    // Type du type de formulaire
    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    private String type;

    // Liste des items du type de formulaire
    @OneToMany(targetEntity = FormItem.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_FormItem_TypeFormulaire", value = ConstraintMode.CONSTRAINT), name = "typeFormulaire_id", referencedColumnName = "id")
    List<FormItem> formItems;

    // Liste des clients du type de formulaire
    @OneToMany(targetEntity = Client.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_Client_TypeFormulaire", value = ConstraintMode.CONSTRAINT), name = "typeFormulaire_id", referencedColumnName = "id")
    List<Client> clients;

    // Pour la première initialisation
    public TypeFormulaire(String type, List<FormItem> formItems) {
        this.defaut = true;
        this.type = type;
        this.formItems = formItems;
        // Les clients seront initialisés lors de leur import dans la page Clients
        this.clients = new ArrayList<>();
    }

    public TypeFormulaire(boolean defaut, String type, List<FormItem> formItems) {
        this.defaut = defaut;
        this.type = type;
        this.formItems = formItems;
        // Pas de clients pour un formulaire importé du drive
        this.clients = new ArrayList<>();
    }

    public Optional<FormItem> getFormItem(FormItemId formItemId) {
        return formItems.stream()
                .filter(item -> item.getName().equals(formItemId.getName()))
                .findFirst();
    }

    @Override
    public String toString() {
        return SerializeDeserialiseUtils.serializeObjectToJSON(this);
    }

}

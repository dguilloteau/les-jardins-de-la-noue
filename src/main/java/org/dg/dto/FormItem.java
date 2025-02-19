package org.dg.dto;

import org.dg.utils.Converter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class FormItem extends Converter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de l'item
    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String itemId;

    // Nom de l'item
    @Column(columnDefinition = "VARCHAR(32)", nullable = false)
    private String name;

    // Libellé de l'item
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String libelle;

    // L'item est pris en compte
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private boolean checked;

    public FormItem(String itemId, String name, String libelle, boolean checked) {
        this.itemId = itemId;
        this.name = name;
        this.libelle = libelle;
        this.checked = checked;
    }

    // Création d'un item par defaut
    public FormItem(FormItemId formItemId, boolean checked) {
        this.itemId = formItemId.getId();
        this.name = formItemId.getName();
        this.libelle = formItemId.getLibelle();
        this.checked = checked;
    }

    @Override
    public String toString() {
        return super.toString(this);
    }

    @Override
    public FormItem fromJsonString(String json) {
        return Converter.fromJsonString(json, this.getClass());
    }

}

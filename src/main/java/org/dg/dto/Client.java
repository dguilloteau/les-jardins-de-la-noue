package org.dg.dto;

import org.dg.utils.Converter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client extends Converter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nom du client
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String nom;

    // Prénom du client
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String prenom;

    // E-mail du client
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String email;

    @Override
    public String toString() {
        return super.toString(this);
    }

    @Override
    public Client fromJsonString(String json) {
        return Converter.fromJsonString(json, this.getClass());
    }

}

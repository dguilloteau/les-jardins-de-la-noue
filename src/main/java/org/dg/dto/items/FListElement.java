package org.dg.dto.items;

import org.dg.utils.SerializeDeserialiseUtils;

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
public class FListElement {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)")
    private String imageUri;

    @Column(columnDefinition = "VARCHAR(255)")
    private String libelleChoix;

    public FListElement(String imageUri, String libelleChoix) {
        this.imageUri = imageUri;
        this.libelleChoix = libelleChoix;
    }

    @Override
    public String toString() {
        return SerializeDeserialiseUtils.serializeObjectToJSON(this);
    }

}

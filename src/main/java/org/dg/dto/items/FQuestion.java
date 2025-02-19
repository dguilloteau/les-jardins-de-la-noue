package org.dg.dto.items;

import org.dg.dto.FormItem;
import org.dg.dto.FormItemId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FQuestion extends FormItem {

    @Column(columnDefinition = "VARCHAR(255)")
    private String titre;

    public FQuestion(Long id, String itemId, String name, String libelle, boolean checked, String titre) {
        super(id, itemId, name, libelle, checked);
        this.titre = titre;
    }

    public FQuestion(String itemId, String name, String libelle, boolean checked, String titre) {
        super(itemId, name, libelle, checked);
        this.titre = titre;
    }

    public FQuestion(FormItemId formItemId, boolean checked, String titre) {
        super(formItemId, checked);
        this.titre = titre;
    }

}

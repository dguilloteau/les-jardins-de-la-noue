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
public class FText extends FormItem {

    @Column(columnDefinition = "VARCHAR(255)")
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String texte;

    public FText(Long id, String itemId, String name, String libelle, boolean checked, String titre, String texte) {
        super(id, itemId, name, libelle, checked);
        this.titre = titre;
        this.texte = texte;
    }

    public FText(String itemId, String name, String libelle, boolean checked, String titre, String texte) {
        super(itemId, name, libelle, checked);
        this.titre = titre;
        this.texte = texte;
    }

    public FText(FormItemId formItemId, boolean checked, String titre, String texte) {
        super(formItemId, checked);
        this.titre = titre;
        this.texte = texte;
    }

}

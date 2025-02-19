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
public class FImage extends FormItem {

    @Column(columnDefinition = "VARCHAR(255)")
    private String titre;

    @Column(columnDefinition = "VARCHAR(255)")
    private String imageUri;

    public FImage(Long id, String itemId, String name, String libelle, boolean checked, String titre, String imageUri) {
        super(id, itemId, name, libelle, checked);
        this.titre = titre;
        this.imageUri = imageUri;
    }

    public FImage(String itemId, String name, String libelle, boolean checked, String titre, String imageUri) {
        super(itemId, name, libelle, checked);
        this.titre = titre;
        this.imageUri = imageUri;
    }

    public FImage(FormItemId formItemId, boolean checked, String titre, String imageUri) {
        super(formItemId, checked);
        this.titre = titre;
        this.imageUri = imageUri;
    }

}

package org.dg.dto.items;

import java.util.ArrayList;
import java.util.List;

import org.dg.dto.FormItem;
import org.dg.dto.FormItemId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FList extends FormItem {

    @Column(columnDefinition = "VARCHAR(255)")
    private String titre;

    @Column(columnDefinition = "VARCHAR(255)")
    private String type;

    @OneToMany(targetEntity = FListElement.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fList_id", referencedColumnName = "id", nullable = false)
    private List<FListElement> listeChoix = new ArrayList<>();

    public FList(Long id, String itemId, String name, String libelle, boolean checked, String titre, String type) {
        super(id, itemId, name, libelle, checked);
        this.titre = titre;
        this.type = type;
    }

    public FList(String itemId, String name, String libelle, boolean checked, String titre, String type) {
        super(itemId, name, libelle, checked);
        this.titre = titre;
        this.type = type;
    }

    public FList(FormItemId formItemId, boolean checked, String titre, String type) {
        super(formItemId, checked);
        this.titre = titre;
        this.type = type;
    }

    public FList addFListToFListElement(FListElement fListElement) {
        this.getListeChoix().add(fListElement);
        return this;
    }

}

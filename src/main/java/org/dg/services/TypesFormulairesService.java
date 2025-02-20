package org.dg.services;

import java.util.List;

import org.dg.db.TypesFormulairesInit;
import org.dg.dto.FormItem;
import org.dg.dto.TypeFormulaire;
import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;
import org.dg.repositories.FormItemRepository;
import org.dg.repositories.TypeFormulaireRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TypesFormulairesService {

    private final TypeFormulaireRepository typeFormulaireRepository;
    private final FormItemRepository formItemRepository;

    @Inject
    public TypesFormulairesService(TypeFormulaireRepository typeFormulaireRepository,
            FormItemRepository formItemRepository) {
        this.typeFormulaireRepository = typeFormulaireRepository;
        this.formItemRepository = formItemRepository;
        if (typeFormulaireRepository.getAllDefaultTypeFormulaire().isEmpty()) {
            Log.debug("Install default types formulaires");
            TypesFormulairesInit.initTypesFormulaires().stream()
                    .forEach(typeFormulaireRepository::save);
        }
    }

    public List<TypeFormulaire> getAllDefaultTypeFormulaire() {
        return typeFormulaireRepository.getAllDefaultTypeFormulaire();
    }

    public TypeFormulaire getFormType(Long id) {
        try {
            return typeFormulaireRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new ShopsException(ShopsErrors.SQL_ERROR, "Le type de formulaire n'a pas été trouvé", e);
        }
    }

    public void patchFormType(String body) {
        FormItem formItem = new FormItem().fromJsonString(body);
        Log.debug("formItem = " + formItem);
        formItemRepository.save(formItem);
    }
}

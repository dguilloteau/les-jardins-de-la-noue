package org.dg.repositories;

import org.dg.dto.Formulaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface FormulaireRepository extends CrudRepository<Formulaire, Long> {

    @Query("from Formulaire where formId = ?1")
    Formulaire getFormulaireByFormId(String formId);

}

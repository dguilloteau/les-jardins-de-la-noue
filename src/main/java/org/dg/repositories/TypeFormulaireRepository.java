package org.dg.repositories;

import java.util.List;

import org.dg.dto.TypeFormulaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.persistence.OrderBy;

public interface TypeFormulaireRepository extends CrudRepository<TypeFormulaire, Long> {

    /**
     * Retourne tous les enregistrements par defaut de la table TypeFormulaire
     * 
     * @return List<TypeFormulaire>
     */
    @Query("select tf from TypeFormulaire tf where defaut = true")
    @OrderBy("id ASC")
    public List<TypeFormulaire> getAllDefaultTypeFormulaire();

    /**
     * Retourne le TypeFormulaire par défaut en fonction de son type
     * 
     * @param name
     * @return TypeFormulaire
     */
    @Query("from TypeFormulaire where type = ?1 and defaut = true")
    public TypeFormulaire getDefaultTypeFormulaireByType(String type);

    /**
     * Return les types de TypeFormulaire par défaut.
     * 
     * @param titre
     * @return TypeFormulaire
     */
    @Query("select tf.type from TypeFormulaire tf where defaut = true")
    public List<String> getDefaultTypesFromTitre();

}

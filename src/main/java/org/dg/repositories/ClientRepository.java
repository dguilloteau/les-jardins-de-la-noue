package org.dg.repositories;

import java.util.List;

import org.dg.dto.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    public Client findByEmail(String email);

}

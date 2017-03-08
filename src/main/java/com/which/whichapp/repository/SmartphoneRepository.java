package com.which.whichapp.repository;

import com.which.whichapp.domain.Smartphone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Smartphone entity.
 */
@SuppressWarnings("unused")
public interface SmartphoneRepository extends JpaRepository<Smartphone,Long> {

    // Buscar Smartphone por nombre
    List<Smartphone> findByModeloContaining(String modelo);

}

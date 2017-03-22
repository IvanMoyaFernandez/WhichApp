package com.which.whichapp.repository;

import com.which.whichapp.domain.Smartphone;

import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumOS;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Smartphone entity.
 */
@SuppressWarnings("unused")
public interface SmartphoneRepository extends JpaRepository<Smartphone,Long> {

    // Buscar Smartphone por modelo
    List<Smartphone> findByModeloContaining(String modelo);
    // Buscar Smartphone por marca
    List<Smartphone> findByMarcaLike(EnumMarca marca);
    // Buscar Smartphone por so
    List<Smartphone> findBySoLike(EnumOS so);
}

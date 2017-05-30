package com.which.whichapp.repository;

import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumOS;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface SmartphoneRepository extends JpaRepository<Smartphone,Long> {
    // Buscar Smartphone por modelo
    List<Smartphone> findByModeloContaining(String modelo);

    // Buscar Smartphone por marca
    List<Smartphone> findByMarcaLike(EnumMarca marca);

    // Buscar Smartphone por sistema operativo
    List<Smartphone> findBySoLike(EnumOS os);

    // Devolver el top 5 smart (puntuación)
    @Query(value = "SELECT smartphone " +
        "FROM Smartphone smartphone ORDER BY smartphone.puntuacion DESC")
    List<Smartphone> getSmartphonesOrderByPuntuacion();
}

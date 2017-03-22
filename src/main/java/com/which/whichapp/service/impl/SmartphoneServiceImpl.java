package com.which.whichapp.service.impl;

import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumOS;
import com.which.whichapp.service.SmartphoneService;
import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.repository.SmartphoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Smartphone.
 */
@Service
@Transactional
public class SmartphoneServiceImpl implements SmartphoneService{

    private final Logger log = LoggerFactory.getLogger(SmartphoneServiceImpl.class);

    @Inject
    private SmartphoneRepository smartphoneRepository;

    /**
     * Save a smartphone.
     *
     * @param smartphone the entity to save
     * @return the persisted entity
     */
    public Smartphone save(Smartphone smartphone) {
        log.debug("Request to save Smartphone : {}", smartphone);
        Smartphone result = smartphoneRepository.save(smartphone);
        return result;
    }

    /**
     *  Get all the smartphones.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Smartphone> findAll() {
        log.debug("Request to get all Smartphones");
        List<Smartphone> result = smartphoneRepository.findAll();

        return result;
    }

    /**
     *  Get one smartphone by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Smartphone findOne(Long id) {
        log.debug("Request to get Smartphone : {}", id);
        Smartphone smartphone = smartphoneRepository.findOne(id);
        return smartphone;
    }

    /**
     *  Delete the  smartphone by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Smartphone : {}", id);
        smartphoneRepository.delete(id);
    }

    @Override
    public List<Smartphone> findByModeloContaining(String modelo) {
        List<Smartphone> modelos = smartphoneRepository.findByModeloContaining(modelo);
        return modelos;
    }

    @Override
        public List<Smartphone> findByMarcaLike(EnumMarca marca) { // --> SmartphoneService.java
        List<Smartphone> modelos = smartphoneRepository.findByMarcaLike(marca);
        return modelos;
    }

    @Override
    public List<Smartphone> findBySoLike(EnumOS so) { // --> SmartphoneService.java
        List<Smartphone> modelos = smartphoneRepository.findBySoLike(so);
        return modelos;
    }
}

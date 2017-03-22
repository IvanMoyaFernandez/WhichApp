package com.which.whichapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.repository.SmartphoneRepository;
import com.which.whichapp.service.SmartphoneService;
import com.which.whichapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Smartphone.
 */
@RestController
@RequestMapping("/api")
public class SmartphoneResource {

    private final Logger log = LoggerFactory.getLogger(SmartphoneResource.class);

    @Inject
    private SmartphoneService smartphoneService;
    private SmartphoneRepository smartphoneRepository;

    /**
     * POST  /smartphones : Create a new smartphone.
     *
     * @param smartphone the smartphone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smartphone, or with status 400 (Bad Request) if the smartphone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smartphones")
    @Timed
    public ResponseEntity<Smartphone> createSmartphone(@Valid @RequestBody Smartphone smartphone) throws URISyntaxException {
        log.debug("REST request to save Smartphone : {}", smartphone);
        if (smartphone.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("smartphone", "idexists", "A new smartphone cannot already have an ID")).body(null);
        }
        Smartphone result = smartphoneService.save(smartphone);
        return ResponseEntity.created(new URI("/api/smartphones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("smartphone", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smartphones : Updates an existing smartphone.
     *
     * @param smartphone the smartphone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smartphone,
     * or with status 400 (Bad Request) if the smartphone is not valid,
     * or with status 500 (Internal Server Error) if the smartphone couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smartphones")
    @Timed
    public ResponseEntity<Smartphone> updateSmartphone(@Valid @RequestBody Smartphone smartphone) throws URISyntaxException {
        log.debug("REST request to update Smartphone : {}", smartphone);
        if (smartphone.getId() == null) {
            return createSmartphone(smartphone);
        }
        Smartphone result = smartphoneService.save(smartphone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("smartphone", smartphone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smartphones : get all the smartphones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartphones in body
     */
    @GetMapping("/smartphones")
    @Timed
    public List<Smartphone> getAllSmartphones() {
        log.debug("REST request to get all Smartphones");
        return smartphoneService.findAll();
    }

    /**
     * GET  /smartphones/:id : get the "id" smartphone.
     *
     * @param id the id of the smartphone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smartphone, or with status 404 (Not Found)
     */
    @GetMapping("/smartphones/{id}")
    @Timed
    public ResponseEntity<Smartphone> getSmartphone(@PathVariable Long id) {
        log.debug("REST request to get Smartphone : {}", id);
        Smartphone smartphone = smartphoneService.findOne(id);
        return Optional.ofNullable(smartphone)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /smartphones/:id : delete the "id" smartphone.
     *
     * @param id the id of the smartphone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smartphones/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmartphone(@PathVariable Long id) {
        log.debug("REST request to delete Smartphone : {}", id);
        smartphoneService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("smartphone", id.toString())).build();
    }

// FILTROS

    // Devolver Smartphones que coincidan con el modelo
    @GetMapping("/smartphones/byModelo/{modelo}")
    @Timed
    public List<Smartphone> getSmartphoneByModelo(@PathVariable String modelo) {
        log.debug("REST request to get Smartphone : {}", modelo);
        List<Smartphone> modelos = smartphoneService.findByModeloContaining(modelo);
        return modelos;
    }

    // Devolver Smartphones que coincidan con la marca --> SmartphoneServiceImpl.java
    @GetMapping("/smartphones/byMarca/{marca}")
    @Timed
    public List<Smartphone> getSmartphoneByMarca(@PathVariable EnumMarca marca) {
        log.debug("REST request to get Smartphone : {}", marca);
        List<Smartphone> modelos = smartphoneService.findByMarcaLike(marca);
        return modelos;
    }
}

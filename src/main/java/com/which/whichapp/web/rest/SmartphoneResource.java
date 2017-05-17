package com.which.whichapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumOS;
import com.which.whichapp.repository.SmartphoneCriteriaRepository;
import com.which.whichapp.service.SmartphoneService;
import com.which.whichapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.Property;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * REST controller for managing Smartphone.
 */
@RestController
@RequestMapping("/api")
public class SmartphoneResource {

    private final Logger log = LoggerFactory.getLogger(SmartphoneResource.class);

    @Inject
    private SmartphoneService smartphoneService;
    @Inject
    private SmartphoneCriteriaRepository smartphoneCriteriaRepository;

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
            .map(resultado -> new ResponseEntity<>(
                resultado,
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


    // Devolver Smartphones que coincidan con el modelo --> SmartphoneServiceImpl.java
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

    // Devolver Smartphones que coincidan con el sistema operativo --> SmartphoneServiceImpl.java
    @GetMapping("/smartphones/bySo/{so}")
    @Timed
    public List<Smartphone> getSmartphoneBySo(@PathVariable EnumOS so) {
        log.debug("REST request to get Smartphone : {}", so);
        List<Smartphone> modelos = smartphoneService.findBySoLike(so);
        return modelos;
    }

    // Devolver Smartphones que coincidan con los criterios de busqueda --> SmartphoneCriteriaRepository.java
    @RequestMapping(value = "/smartphones/byFiltros/",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<List<Smartphone>> getSmartphonesByFiltros(
        @RequestParam(value = "so", required = false) String so,
        @RequestParam(value = "marca", required = false) String marca,
        @RequestParam(value = "camara", required = false) String camara,
        @RequestParam(value = "front_camara", required = false) String front_camara,
        @RequestParam(value = "rom", required = false) String rom
    ){
        Map<String, Object> parametros = new HashMap<>();

        // primero comprobamos que el parametro no venga vacio ni en null, si viene con algun parametro entramos en el if
        if (so != null && !so.isEmpty() && !so.equals("empty")){
            // guardamos en la variable soSplitEnum el resultados que hemos recibido que vienen separados por un guion,
            // pero los guardamos sin los guiones.
            EnumOS[] soSplitEnum = Stream.of(so.split("-")).map(brand ->{
                try {
                    return EnumOS.valueOf(brand);
                } catch (IllegalArgumentException e){
                    log.error("SO no existe : {}", brand);
                    throw e;
                }
                // convertimos a array de EnumOS el resultado que ha quedado de separar los parametros que venian separados por guiones.
            }).toArray(EnumOS[]::new);
            parametros.put("so", soSplitEnum);
        }

        if (marca != null && !marca.isEmpty() && !marca.equals("empty")){
            EnumMarca[] marcaSplitEnum = Stream.of(marca.split("-")).map(brand ->{
                try {
                    return EnumMarca.valueOf(brand);
                } catch (IllegalArgumentException e){
                    log.error("Marca no existe : {}", brand);
                    throw e;
                }
            }).toArray(EnumMarca[]::new);
            parametros.put("marcas", marcaSplitEnum);
        }
        if (camara != null && !camara.isEmpty() && !camara.equals("empty")){
            String[] camaraSplit = camara.split("-");
            Integer[] camaraSplitInteger = Stream.of(camaraSplit).map(s -> Integer.parseInt(s)).toArray(Integer[]::new);
            parametros.put("camaras", camaraSplitInteger);
        }
        if (front_camara != null && !front_camara.isEmpty() && !front_camara.equals("empty")){
            String[] front_camaraSplit = front_camara.split("-");
            Integer[] front_camaraSplitInteger = Stream.of(front_camaraSplit).map(s -> Integer.parseInt(s)).toArray(Integer[]::new);
            parametros.put("front_camaras", front_camaraSplitInteger);
        }
        if (rom != null && !rom.isEmpty() && !rom.equals("empty")){
            String[] romSplit = rom.split("-");
            Integer[] romSplitInteger = Stream.of(romSplit).map(s -> Integer.parseInt(s)).toArray(Integer[]::new);
            parametros.put("roms", romSplitInteger);
        }

        List<Smartphone> resultadoFiltroBusqueda = smartphoneCriteriaRepository.buscarSmartphonesByFiltros(parametros);

        return Optional.ofNullable(resultadoFiltroBusqueda)
            .map(resultado -> new ResponseEntity<>(
                resultado,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

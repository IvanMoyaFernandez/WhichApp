package com.which.whichapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.TreeMultimap;
import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumOS;
import com.which.whichapp.repository.SmartphoneCriteriaRepository;
import com.which.whichapp.repository.SmartphoneRepository;
import com.which.whichapp.service.SmartphoneService;
import com.which.whichapp.service.dto.PuntuacionSmartphone;
import com.which.whichapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
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
    @Inject
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
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("smartphone", "idexists",
                "A new smartphone cannot already have an ID")).body(null);
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

    // Devolver todos los smartphones ordenados por puntuacion mediante un TreeMultimap<Integer, Smartphone>;
    @GetMapping("/orderByPuntuacion")
    @Timed
    public List<PuntuacionSmartphone> getSmartphonesOrderByPuntuacion() {
        // agrupa la lista recibida en un map de forma paralela (los smartphones están agrupados por puntuación
        // y las puntuaciónes desordenadas)
        Map<Integer, List<Smartphone>> mapSmartphones = smartphoneRepository.findAll()
            .parallelStream()
            .collect(Collectors.groupingBy(Smartphone::getPuntuacion));
        // creamos el ArrayList que posteriormente contendrá la lista de smartphones ordenados desc
        List<PuntuacionSmartphone> result = new ArrayList<>();
        // recorremos los resultados del Map - mapSmartphones - y vamos añadiendo CADA RESULTADO*** dentro del ArrayList - result -
        // ***(CADA RESULTADO es un array de smartphones agrupados por la puntuación, esto es un objeto de la clase PuntuacionSmartphone)
        for (Integer puntuacion :
            mapSmartphones.keySet().stream().limit(5).sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList())
             ) {
            PuntuacionSmartphone puntuacionSmartphone = new PuntuacionSmartphone(puntuacion, mapSmartphones.get(puntuacion));
            result.add(puntuacionSmartphone);
        }
        //  Devolvemos el ArrayList - result - con todos los smartphones ordenados por su key (la puntuación)
        return result;
    }

    // Devolver todos los smartphones ordenados por puntuacion mediante un TreeMultimap<Integer, Smartphone>;
    @GetMapping("/orderAllByPuntuacion")
    @Timed
    public List<PuntuacionSmartphone> getAllSmartphonesOrderByPuntuacion() {
        Map<Integer, List<Smartphone>> mapSmartphones = smartphoneRepository.findAll()
            .parallelStream()
            .collect(Collectors.groupingBy(Smartphone::getPuntuacion));
        List<PuntuacionSmartphone> result = new ArrayList<>();
        for (Integer puntuacion :
            mapSmartphones.keySet().stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList())) {
            PuntuacionSmartphone puntuacionSmartphone = new PuntuacionSmartphone(puntuacion, mapSmartphones.get(puntuacion));
            result.add(puntuacionSmartphone);
        }
        return result;
    }

    // Devolver Smartphones que coincidan con los criterios de busqueda --> SmartphoneCriteriaRepository.java
    @RequestMapping(value = "/smartphones/byFiltros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<List<Smartphone>> getSmartphonesByFiltros(
        @RequestParam(value = "so", required = false) String so,
        @RequestParam(value = "marca", required = false) String marca,
        @RequestParam(value = "camara", required = false) String camara,
        @RequestParam(value = "front_camara", required = false) String front_camara,
        @RequestParam(value = "minMemoria", required = false) String minMemoria,
        @RequestParam(value = "maxMemoria", required = false) String maxMemoria,
        @RequestParam(value = "minPuntuacion", required = false) String minPuntuacion,
        @RequestParam(value = "maxPuntuacion", required = false) String maxPuntuacion
    ){
        Map<String, Object> parametros = new HashMap<>();

        // primero comprobamos que el parametro no venga vacio ni en null, si viene con algun parametro entramos en el if
        if (so != null && !so.isEmpty() && !so.equals("empty")){
            // guardamos en la variable soSplitEnum el resultados que hemos recibido que vienen separados por un guion,
            // pero los guardamos sin los guiones.
            try {
                // convertimos el array de strings a array de enums
                EnumOS[] soSplitEnum = Stream.of(so.split("-")).map(system -> EnumOS.valueOf(system)).toArray(EnumOS[]::new);
                parametros.put("so", soSplitEnum);
            } catch (IllegalArgumentException e){
                log.error("SO no existe : {}");
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("Smartphone", "systemNotExist", e.getMessage())).body(null);
            }
        }
        if (marca != null && !marca.isEmpty() && !marca.equals("empty")){
            try {
                EnumMarca[] marcaSplitEnum = Stream.of(marca.split("-")).map(brand -> EnumMarca.valueOf(brand)).toArray(EnumMarca[]::new);
                parametros.put("marca", marcaSplitEnum);
            } catch (IllegalArgumentException e){
                log.error("Marca no existe : {}");
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("Smartphone", "marcaNotExist", e.getMessage())).body(null);
            }
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
        if (minMemoria != null && !minMemoria.isEmpty() && ! minMemoria.equals("empty")) {
            Integer minMemoriaInteger = Integer.parseInt(minMemoria);
            parametros.put("minMemoria", minMemoriaInteger);
        }
        if (maxMemoria != null && !maxMemoria.isEmpty() && ! maxMemoria.equals("empty")) {
            Integer maxMemoriaInteger = Integer.parseInt(maxMemoria);
            parametros.put("maxMemoria", maxMemoriaInteger);
        }
        if (minPuntuacion != null && !minPuntuacion.isEmpty() && ! minPuntuacion.equals("empty")) {
            Integer minPuntuacionInteger = Integer.parseInt(minPuntuacion);
            parametros.put("minPuntuacion", minPuntuacionInteger);
        }
        if (maxPuntuacion != null && !maxPuntuacion.isEmpty() && ! maxPuntuacion.equals("empty")) {
            Integer maxPuntuacionInteger = Integer.parseInt(maxPuntuacion);
            parametros.put("maxPuntuacion", maxPuntuacionInteger);
        }

        List<Smartphone> resultadoFiltroBusqueda = smartphoneCriteriaRepository.buscarSmartphonesByFiltros(parametros);

        return Optional.ofNullable(resultadoFiltroBusqueda)
            .map(resultado -> new ResponseEntity<>(
                resultado,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

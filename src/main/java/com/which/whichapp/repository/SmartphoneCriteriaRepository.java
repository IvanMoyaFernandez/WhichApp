package com.which.whichapp.repository;

import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumOS;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class SmartphoneCriteriaRepository{
    @PersistenceContext
    EntityManager entityManager;

    protected Session currentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<Smartphone> buscarSmartphonesByFiltros(Map<String, Object> parametros) {

        Criteria smartphoneDefinitionCriteria = currentSession().createCriteria(Smartphone.class);
        smartphoneDefinitionCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        filtroBySo(parametros, smartphoneDefinitionCriteria, "so");
        filtroByMarca(parametros, smartphoneDefinitionCriteria,"marca");
        filtroByCamara(parametros, smartphoneDefinitionCriteria);
        filtroByFrontCamara(parametros, smartphoneDefinitionCriteria);
        filterByRomBetween(parametros, smartphoneDefinitionCriteria);
        filterByPuntuacionBetween(parametros, smartphoneDefinitionCriteria);

        List<Smartphone>  resultados = smartphoneDefinitionCriteria.list();

        return resultados;
    }

    private void filtroByCamara(Map<String, Object> parametros, Criteria smartphoneDefinitionCriteria) {
        if (parametros.containsKey("camaras")) {
            Integer[] camaras = (Integer[]) parametros.get("camaras");
            smartphoneDefinitionCriteria.add(Restrictions.in("camara", camaras));
        }
    }
    private void filtroByFrontCamara(Map<String, Object> parametros, Criteria smartphoneDefinitionCriteria) {
        if (parametros.containsKey("front_camaras")) {
            Integer[] front_camaras = (Integer[]) parametros.get("front_camaras");
            smartphoneDefinitionCriteria.add(Restrictions.in("front_camara", front_camaras));
        }
    }
    private void filterByRomBetween(Map<String,Object> parametros, Criteria propertyCriteria) {
        if (parametros.containsKey("minMemoria") && parametros.containsKey("maxMemoria")) {
            Integer minMemoria = (Integer) parametros.get("minMemoria");
            Integer maxMemoria = (Integer) parametros.get("maxMemoria");
            propertyCriteria.add(Restrictions.between("rom", minMemoria, maxMemoria));
        }
    }
    private void filtroBySo (Map<String,Object> parameters, Criteria propertyCriteria, String key){
        if (parameters.containsKey(key)) {
            EnumOS[] enumOSes = (EnumOS[]) parameters.get(key);
            propertyCriteria.add(Restrictions.in("so", enumOSes));
        }
    }
    private void filtroByMarca (Map<String,Object> parametros, Criteria propertyCriteria, String key){
        if (parametros.containsKey(key)) {
            EnumMarca[] enumMarcas = (EnumMarca[]) parametros.get(key);
            propertyCriteria.add(Restrictions.in("marca", enumMarcas));
        }
    }
    private void filterByPuntuacionBetween(Map<String,Object> parametros, Criteria propertyCriteria) {
        if (parametros.containsKey("minPuntuacion") && parametros.containsKey("maxPuntuacion")) {
            Integer minPuntuacion = (Integer) parametros.get("minPuntuacion");
            Integer maxPuntuacion = (Integer) parametros.get("maxPuntuacion");
            propertyCriteria.add(Restrictions.between("puntuacion", minPuntuacion, maxPuntuacion));
        }
    }
}

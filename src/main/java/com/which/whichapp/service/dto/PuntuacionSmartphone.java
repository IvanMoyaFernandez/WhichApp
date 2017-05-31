package com.which.whichapp.service.dto;

import com.which.whichapp.domain.Smartphone;

import java.util.Collection;

public class PuntuacionSmartphone {

    private Integer puntuacion;
    private Collection<Smartphone> smartphones;

    public PuntuacionSmartphone(Integer puntuacion, Collection<Smartphone> smartphones) {
        this.puntuacion = puntuacion;
        this.smartphones = smartphones;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Collection<Smartphone> getSmartphones() {
        return smartphones;
    }

    public void setSmartphones(Collection<Smartphone> smartphones) {
        this.smartphones = smartphones;
    }

    @Override
    public String toString() {
        return "PuntuacionSmartphone{" +
            "puntuacion=" + puntuacion +
            ", smartphones=" + smartphones +
            '}';
    }
}

package com.which.whichapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.which.whichapp.domain.enumeration.EnumMarca;

import com.which.whichapp.domain.enumeration.EnumResolucionPantalla;

import com.which.whichapp.domain.enumeration.EnumOS;

/**
 * A Smartphone.
 */
@Entity
@Table(name = "smartphone")
public class Smartphone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "marca", nullable = false)
    private EnumMarca marca;

    @NotNull
    @Column(name = "camara", nullable = false)
    private Double camara;

    @NotNull
    @Column(name = "front_camara", nullable = false)
    private Double frontCamara;

    @NotNull
    @Column(name = "bateria", nullable = false)
    private Integer bateria;

    @NotNull
    @Column(name = "pulgadas_pantalla", nullable = false)
    private Double pulgadasPantalla;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "resolucion_pantalla", nullable = false)
    private EnumResolucionPantalla resolucionPantalla;

    @NotNull
    @Column(name = "ram", nullable = false)
    private Integer ram;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "so", nullable = false)
    private EnumOS so;

    @NotNull
    @Column(name = "rom", nullable = false)
    private Integer rom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public Smartphone modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public EnumMarca getMarca() {
        return marca;
    }

    public Smartphone marca(EnumMarca marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(EnumMarca marca) {
        this.marca = marca;
    }

    public Double getCamara() {
        return camara;
    }

    public Smartphone camara(Double camara) {
        this.camara = camara;
        return this;
    }

    public void setCamara(Double camara) {
        this.camara = camara;
    }

    public Double getFrontCamara() {
        return frontCamara;
    }

    public Smartphone frontCamara(Double frontCamara) {
        this.frontCamara = frontCamara;
        return this;
    }

    public void setFrontCamara(Double frontCamara) {
        this.frontCamara = frontCamara;
    }

    public Integer getBateria() {
        return bateria;
    }

    public Smartphone bateria(Integer bateria) {
        this.bateria = bateria;
        return this;
    }

    public void setBateria(Integer bateria) {
        this.bateria = bateria;
    }

    public Double getPulgadasPantalla() {
        return pulgadasPantalla;
    }

    public Smartphone pulgadasPantalla(Double pulgadasPantalla) {
        this.pulgadasPantalla = pulgadasPantalla;
        return this;
    }

    public void setPulgadasPantalla(Double pulgadasPantalla) {
        this.pulgadasPantalla = pulgadasPantalla;
    }

    public EnumResolucionPantalla getResolucionPantalla() {
        return resolucionPantalla;
    }

    public Smartphone resolucionPantalla(EnumResolucionPantalla resolucionPantalla) {
        this.resolucionPantalla = resolucionPantalla;
        return this;
    }

    public void setResolucionPantalla(EnumResolucionPantalla resolucionPantalla) {
        this.resolucionPantalla = resolucionPantalla;
    }

    public Integer getRam() {
        return ram;
    }

    public Smartphone ram(Integer ram) {
        this.ram = ram;
        return this;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public EnumOS getSo() {
        return so;
    }

    public Smartphone so(EnumOS so) {
        this.so = so;
        return this;
    }

    public void setSo(EnumOS so) {
        this.so = so;
    }

    public Integer getRom() {
        return rom;
    }

    public Smartphone rom(Integer rom) {
        this.rom = rom;
        return this;
    }

    public void setRom(Integer rom) {
        this.rom = rom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Smartphone smartphone = (Smartphone) o;
        if (smartphone.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, smartphone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Smartphone{" +
            "id=" + id +
            ", modelo='" + modelo + "'" +
            ", marca='" + marca + "'" +
            ", camara='" + camara + "'" +
            ", frontCamara='" + frontCamara + "'" +
            ", bateria='" + bateria + "'" +
            ", pulgadasPantalla='" + pulgadasPantalla + "'" +
            ", resolucionPantalla='" + resolucionPantalla + "'" +
            ", ram='" + ram + "'" +
            ", so='" + so + "'" +
            ", rom='" + rom + "'" +
            '}';
    }
}

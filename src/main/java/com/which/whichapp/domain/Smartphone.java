package com.which.whichapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.which.whichapp.domain.enumeration.EnumMarca;

import com.which.whichapp.domain.enumeration.EnumOS;

@Entity
@Table(name = "smartphone")
public class Smartphone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "marca", nullable = false)
    private EnumMarca marca;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @NotNull
    @Column(name = "camara", nullable = false)
    private Integer camara;

    @NotNull
    @Column(name = "front_camara", nullable = false)
    private Integer frontCamara;

    @NotNull
    @Column(name = "bateria", nullable = false)
    private Integer bateria;

    @NotNull
    @Column(name = "pulgadas_pantalla", nullable = false)
    private Double pulgadasPantalla;

    @NotNull
    @Column(name = "resolucion_pantalla_alto", nullable = false)
    private Integer resolucionPantallaAlto;

    @NotNull
    @Column(name = "resolucion_pantalla_ancho", nullable = false)
    private Integer resolucionPantallaAncho;

    @NotNull
    @Column(name = "ram", nullable = false)
    private Double ram;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "so", nullable = false)
    private EnumOS so;

    @NotNull
    @Column(name = "rom", nullable = false)
    private Integer rom;

    @NotNull
    @Column(name = "proteccion_polvo", nullable = false)
    private Integer proteccionPolvo;

    @NotNull
    @Column(name = "proteccion_liquido", nullable = false)
    private Integer proteccionLiquido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getCamara() {
        return camara;
    }

    public Smartphone camara(Integer camara) {
        this.camara = camara;
        return this;
    }

    public void setCamara(Integer camara) {
        this.camara = camara;
    }

    public Integer getFrontCamara() {
        return frontCamara;
    }

    public Smartphone frontCamara(Integer frontCamara) {
        this.frontCamara = frontCamara;
        return this;
    }

    public void setFrontCamara(Integer frontCamara) {
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

    public Integer getResolucionPantallaAlto() {
        return resolucionPantallaAlto;
    }

    public Smartphone resolucionPantallaAlto(Integer resolucionPantallaAlto) {
        this.resolucionPantallaAlto = resolucionPantallaAlto;
        return this;
    }

    public void setResolucionPantallaAlto(Integer resolucionPantallaAlto) {
        this.resolucionPantallaAlto = resolucionPantallaAlto;
    }

    public Integer getResolucionPantallaAncho() {
        return resolucionPantallaAncho;
    }

    public Smartphone resolucionPantallaAncho(Integer resolucionPantallaAncho) {
        this.resolucionPantallaAncho = resolucionPantallaAncho;
        return this;
    }

    public void setResolucionPantallaAncho(Integer resolucionPantallaAncho) {
        this.resolucionPantallaAncho = resolucionPantallaAncho;
    }

    public Double getRam() {
        return ram;
    }

    public Smartphone ram(Double ram) {
        this.ram = ram;
        return this;
    }

    public void setRam(Double ram) {
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

    public Integer getProteccionPolvo() {
        return proteccionPolvo;
    }

    public Smartphone proteccionPolvo(Integer proteccionPolvo) {
        this.proteccionPolvo = proteccionPolvo;
        return this;
    }

    public void setProteccionPolvo(Integer proteccionPolvo) {
        this.proteccionPolvo = proteccionPolvo;
    }

    public Integer getProteccionLiquido() {
        return proteccionLiquido;
    }

    public Smartphone proteccionLiquido(Integer proteccionLiquido) {
        this.proteccionLiquido = proteccionLiquido;
        return this;
    }

    public void setProteccionLiquido(Integer proteccionLiquido) {
        this.proteccionLiquido = proteccionLiquido;
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
            ", marca='" + marca + "'" +
            ", modelo='" + modelo + "'" +
            ", camara='" + camara + "'" +
            ", frontCamara='" + frontCamara + "'" +
            ", bateria='" + bateria + "'" +
            ", pulgadasPantalla='" + pulgadasPantalla + "'" +
            ", resolucionPantallaAlto='" + resolucionPantallaAlto + "'" +
            ", resolucionPantallaAncho='" + resolucionPantallaAncho + "'" +
            ", ram='" + ram + "'" +
            ", so='" + so + "'" +
            ", rom='" + rom + "'" +
            ", proteccionPolvo='" + proteccionPolvo + "'" +
            ", proteccionLiquido='" + proteccionLiquido + "'" +
            '}';
    }
}

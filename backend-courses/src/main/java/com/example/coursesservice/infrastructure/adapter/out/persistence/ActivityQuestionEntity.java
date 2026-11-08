package com.example.coursesservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity mapped to the {@code actividad_preguntas} table.
 */
@Entity
@Table(name = "actividad_preguntas")
public class ActivityQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actividad_id", nullable = false)
    private Long actividadId;

    @Column(nullable = false)
    private int orden;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, columnDefinition = "text")
    private String enunciado;

    @Column(nullable = false, columnDefinition = "text")
    private String configuracion;

    @Column(name = "medio_tipo", length = 10)
    private String medioTipo;

    @Column(name = "medio_url", length = 500)
    private String medioUrl;

    @Column(name = "medio_alt", columnDefinition = "text")
    private String medioAlt;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(final Long actividadId) {
        this.actividadId = actividadId;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(final int orden) {
        this.orden = orden;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(final String enunciado) {
        this.enunciado = enunciado;
    }

    public String getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(final String configuracion) {
        this.configuracion = configuracion;
    }

    public String getMedioTipo() {
        return medioTipo;
    }

    public void setMedioTipo(final String medioTipo) {
        this.medioTipo = medioTipo;
    }

    public String getMedioUrl() {
        return medioUrl;
    }

    public void setMedioUrl(final String medioUrl) {
        this.medioUrl = medioUrl;
    }

    public String getMedioAlt() {
        return medioAlt;
    }

    public void setMedioAlt(final String medioAlt) {
        this.medioAlt = medioAlt;
    }
}

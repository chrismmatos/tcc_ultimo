package com.example.christian.tcc.modelo;

/**
 * Created by christian on 11/03/2018.
 */

public class Voluntario {
    private Integer IdVoluntario;
    private String Nome;
    private Double Latitude;
    private Double Longitude;

    public Integer getIdVoluntario() {
        return IdVoluntario;
    }

    public void setIdVoluntario(Integer idVoluntario) {
        IdVoluntario = idVoluntario;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
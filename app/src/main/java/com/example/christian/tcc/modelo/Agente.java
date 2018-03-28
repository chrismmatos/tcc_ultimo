package com.example.christian.tcc.modelo;

/**
 * Created by christian on 11/03/2018.
 */

public class Agente {
    private Integer IdAgente;
    private String Nome;
    private Double Latitude;
    private Double Longitude;
    private String TipoAgente = "";

    public Integer getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(Integer idAgente) {
        IdAgente = idAgente;
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

    public String getTipoAgente() {
        return TipoAgente;
    }

    public void setTipoAgente(String tipoAgente) {
        TipoAgente = tipoAgente;
    }
}

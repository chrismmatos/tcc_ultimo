package com.example.christian.tcc.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christian on 11/03/2018.
 */


public class Usuario implements Serializable {
    private Integer id = 0;
    private Integer idLogado = 0;
    private String tipoUsuario = "";
    private String tipoAgente = "";
    private String tipoPCD = "";

    public String getTipoAgente() {
        return tipoAgente;
    }

    public void setTipoAgente(String tipoAgente) {
        this.tipoAgente = tipoAgente;
    }

    public String getTipoPCD() {
        return tipoPCD;
    }

    public void setTipoPCD(String tipoPCD) {
        this.tipoPCD = tipoPCD;
    }

    private Integer tempo = 2;
    private Integer distancia = 1000;

    public Usuario(Integer idUsuarioLogado, String tipoUsuario) {
        this.idLogado = idUsuarioLogado;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(Integer id, Integer idLogado, String tipoUsuario, Integer tempo, Integer distancia) {
        this.id = id;
        this.idLogado = idLogado;
        this.tipoUsuario = tipoUsuario;
        this.tempo = tempo;
        this.distancia = distancia;
    }

    public Usuario(){}

    public Map <String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id",id);
        result.put("idLogado",idLogado);
        result.put("tipoUsuario",tipoUsuario);
        result.put("tempo",tempo);
        result.put("distancia",distancia);
        result.put("tipoAgente",tipoAgente);
        result.put("tipoPCD",tipoPCD);

        return  result;
    }

    // Getters e Setters...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdLogado() {
        return idLogado;
    }

    public void setIdLogado(Integer idLogado) {
        this.idLogado = idLogado;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }


}

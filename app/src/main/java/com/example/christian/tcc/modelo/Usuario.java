package com.example.christian.tcc.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christian on 11/03/2018.
 */


public class Usuario implements Serializable {
    private Integer idLogado = 0;
    private String tipoUsuario = "";
    private String tipoAgente = "";
    private String tipoPCD = "";
    private String nome = "";
    private String email = "";
    private Integer numPas = 0;



    public Usuario(){}

    public Usuario(String tipoUsuario, String tipoAgentePCD, String nome, String email) {
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.email = email;

        switch (tipoUsuario){

            case "Pessoa com deficiência":{
                tipoPCD = tipoAgentePCD;
                break;
            }
            case "Agente":{
                tipoAgente = tipoAgentePCD;
                break;
            }
            default:
                break;
        }
    }

    public Usuario(String tipoUsuario, String nome, String email) {
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.email = email;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumPas() {
        return numPas;
    }

    public void setNumPas(Integer numPas) {
        this.numPas = numPas;
    }

    public Map <String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("idLogado",idLogado);
        result.put("tipoUsuario",tipoUsuario);
        result.put("nome",nome);
        result.put("email",email);
        result.put("tipoAgente",tipoAgente);
        result.put("tipoPCD",tipoPCD);
        result.put("numPas",numPas);

        return  result;
    }


}
package com.example.christian.tcc.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.example.christian.tcc.activitys.MainAct.mRootRef;

/**
 * Created by christian on 11/03/2018.
 */


public class Usuario implements Serializable {
    private String id = "";
    private Integer idLogado = 0;
    private String tipoUsuario = "";
    private String tipoAgente = "";
    private String tipoPCD = "";
    private String nome = "";
    private String email = "";
    private Integer numPas = 0;
    private Double latitude = 0.0;
    private Double longitude = 0.0;



    public Usuario(){}

    public Usuario(String id, String email, String nome,  String tipoUsuario, String tipoAgentePCD) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;


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

    public Usuario(String id, String email, String nome, String tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;

    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", idLogado=" + idLogado +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                ", tipoAgente='" + tipoAgente + '\'' +
                ", tipoPCD='" + tipoPCD + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", numPas=" + numPas +
                '}';
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        result.put("id",id);
        result.put("idLogado",idLogado);
        result.put("tipoUsuario",tipoUsuario);
        result.put("nome",nome);
        result.put("email",email);
        result.put("tipoAgente",tipoAgente);
        result.put("tipoPCD",tipoPCD);
        result.put("numPas",numPas);
        result.put("latitude",latitude);
        result.put("longitude",longitude);

        return  result;
    }

    public void salvar(){
        mRootRef.child("usuarios").child(id).setValue(this);
    }

}
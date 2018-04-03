//package com.example.christian.tcc.modelo;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by christian on 11/03/2018.
// */
//
//public class Agente {
//    private Integer id;
//    private Integer idLogado = 0;
//    private String nome;
//    private Double latitude = 0.0;
//    private Double longitude = 0.0;
//    private String tipoAgente = "";
//    private String tipoUsuário = "";
//    private String email = "";
//
//    public Agente() {}
//
//    public String getTipoUsuário() {
//        return tipoUsuário;
//    }
//
//    public void setTipoUsuário(String tipoUsuário) {
//        this.tipoUsuário = tipoUsuário;
//    }
//
//
//    public Agente(Integer id, String nome, String tipoAgente, String tipoUsuário, String email) {
//        this.id = id;
//        this.nome = nome;
//        this.tipoAgente = tipoAgente;
//        this.tipoUsuário = tipoUsuário;
//        this.email = email;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public Integer getIdLogado() {
//        return idLogado;
//    }
//
//    public void setIdLogado(Integer idLogado) {
//        this.idLogado = idLogado;
//    }
//
//    public String getNome() {
//        return nome;
//    }
//
//    public void setNome(String nome) {
//        this.nome = nome;
//    }
//
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getTipoAgente() {
//        return tipoAgente;
//    }
//
//    public void setTipoAgente(String tipoAgente) {
//        this.tipoAgente = tipoAgente;
//    }
//
//    public Map<String, Object> toMap(){
//        HashMap<String,Object> result = new HashMap<>();
//        result.put("id",id);
//        result.put("idLogado",idLogado);
//        result.put("nome",nome);
//        result.put("tipoAgente",tipoAgente);
//        result.put("latitude",latitude);
//        result.put("longitude",longitude);
//        result.put("tipoUsuario",tipoUsuário);
//        result.put("email",email);
//
//        return  result;
//    }
//}

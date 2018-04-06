//package com.example.christian.tcc.modelo;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by christian on 11/03/2018.
// */
//
//public class Pcd {
//    private Integer id;
//    private Integer idLogado = 0;
//    private String nome;
//    private Double latitude = 0.0;
//    private Double longitude = 0.0;
//    private String tipoPcd = "";
//    private String tipoUsuario = "";
//    private String email = "";
//
//
//    public Pcd() {}
//
//    public String getTipoPcd() {
//        return tipoPcd;
//    }
//
//    public void setTipoPcd(String tipoPcd) {
//        this.tipoPcd = tipoPcd;
//    }
//
//    public String getTipoUsuario() {
//        return tipoUsuario;
//    }
//
//    public void setTipoUsuario(String tipoUsuario) {
//        this.tipoUsuario = tipoUsuario;
//    }
//
//    public Pcd(Integer id, String nome, String tipoPcd, String tipoUsuario, String email) {
//        this.id = id;
//        this.nome = nome;
//        this.tipoPcd = tipoPcd;
//        this.tipoUsuario = tipoUsuario;
//        this.email = email;
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
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
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
//        return tipoPcd;
//    }
//
//    public void setTipoAgente(String tipoAgente) {
//        this.tipoPcd = tipoAgente;
//    }
//
//    public Map<String, Object> toMap(){
//        HashMap<String,Object> result = new HashMap<>();
//        result.put("id",id);
//        result.put("idLogado",idLogado);
//        result.put("nome",nome);
//        result.put("tipoPcd",tipoPcd);
//        result.put("latitude",latitude);
//        result.put("longitude",longitude);
//        result.put("tipoUsuario",tipoUsuario);
//        result.put("email",email);
//
//        return  result;
//    }
//}
//
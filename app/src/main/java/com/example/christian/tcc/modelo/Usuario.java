package com.example.christian.tcc.modelo;

import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christian on 11/03/2018.
 */

public class Usuario implements Serializable {
    private String id = "";
    private String tipoUsuario = "";
    private String tipoAgente = "";
    private String tipoPCD = "";
    private String nome = "";
    private String email = "";
    private Integer acompanhamentosRealizados = 0;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private String token = null;
    public boolean deficienciaMental = false;
    public boolean deficienciaMotora = false;
    public boolean deficienciaAuditiva = false;
    public boolean deficienciaVisual = false;
    public boolean deficienciaCadeirante = false;
    private boolean online = false;
    public String vtr = null;

    public boolean isDeficienciaMental() {
        return deficienciaMental;
    }

    public void setDeficienciaMental(boolean deficienciaMental) {
        this.deficienciaMental = deficienciaMental;
    }

    public boolean isDeficienciaMotora() {
        return deficienciaMotora;
    }

    public void setDeficienciaMotora(boolean deficienciaMotora) {
        this.deficienciaMotora = deficienciaMotora;
    }

    public boolean isDeficienciaAuditiva() {
        return deficienciaAuditiva;
    }

    public void setDeficienciaAuditiva(boolean deficienciaAuditiva) {
        this.deficienciaAuditiva = deficienciaAuditiva;
    }

    public boolean isDeficienciaVisual() {
        return deficienciaVisual;
    }

    public void setDeficienciaVisual(boolean deficienciaVisual) {
        this.deficienciaVisual = deficienciaVisual;
    }

    public boolean isDeficienciaCadeirante() {
        return deficienciaCadeirante;
    }

    public void setDeficienciaCadeirante(boolean deficienciaCadeirante) {
        this.deficienciaCadeirante = deficienciaCadeirante;
    }

    public Usuario(){ }

    public String getVtr() {
        return vtr;
    }

    public void setVtr(String vtr) {
        this.vtr = vtr;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Integer getAcompanhamentosRealizados() {
        return acompanhamentosRealizados;
    }

    public void setAcompanhamentosRealizados(Integer acompanhamentosRealizados) {
        this.acompanhamentosRealizados = acompanhamentosRealizados;
    }

    public Map <String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id",id);
        result.put("tipoUsuario",tipoUsuario);
        result.put("nome",nome);
        result.put("email",email);
        result.put("tipoAgente",tipoAgente);
        result.put("tipoPCD",tipoPCD);
        result.put("acompanhamentosRealizados",acompanhamentosRealizados);
        result.put("latitude",latitude);
        result.put("longitude",longitude);

        return  result;
    }

    public void salvar(){
        DatabaseReference mRootRef = ConfiguracaoFirebase.getFirebaseDatabase();
        mRootRef.child("usuarios").child(id).setValue(this);
    }

    public void addAcomp(){
        acompanhamentosRealizados +=1;
    }

    public int getLevel(){
        return (acompanhamentosRealizados/5) + 1;
    }

    public int getProgress(){
        return (acompanhamentosRealizados % 5) * 20;
    }

}
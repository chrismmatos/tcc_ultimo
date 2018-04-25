package com.example.christian.tcc.modelo;

import com.example.christian.tcc.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class PedidoAcompanhamento implements Serializable {
    private String id ;
    private String usuario;
    private String acompanhante = "";
    private boolean ativo = false;
    private boolean concluido = false;
    private String localizacao = "";
    private String data ="";
    private String tipo = "";

    public PedidoAcompanhamento() {


    }

    public String getData() {
        return data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAcompanhante() {
        return acompanhante;
    }

    public void setAcompanhante(String acompanhante) {
        this.acompanhante = acompanhante;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public void salvar(){
        DatabaseReference mRootRef = ConfiguracaoFirebase.getFirebaseDatabase();
        mRootRef.child("pedidos").child(id).setValue(this);
    }

    @Override
    public String toString() {
        return "PedidoAcompanhamento{" +
                "id='" + id + '\'' +
                ", usuario='" + usuario + '\'' +
                ", acompanhante='" + acompanhante + '\'' +
                ", ativo=" + ativo +
                ", concluido=" + concluido +
                '}';
    }
}

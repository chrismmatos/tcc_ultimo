package com.example.christian.tcc.modelo;

public class PedidoAcompanhamento {
    private String id ;
    private String usuario;
    private String acompanhante;
    private boolean isIniciado = false;

    public PedidoAcompanhamento() {
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

    public boolean isIniciado() {
        return isIniciado;
    }

    public void setIniciado(boolean iniciado) {
        isIniciado = iniciado;
    }
}

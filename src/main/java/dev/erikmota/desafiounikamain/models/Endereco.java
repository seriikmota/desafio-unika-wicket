package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Endereco implements Serializable {
    private Long id;
    private String endereco;
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private String principal;
    @JsonProperty("monitorador_id")
    private Monitorador monitorador;

    public Endereco(){

    }
    public Endereco(Endereco e) {
        this.id = e.id;
        this.endereco = e.endereco;
        this.numero = e.numero;
        this.cep = e.cep;
        this.bairro = e.bairro;
        this.telefone = e.telefone;
        this.cidade = e.cidade;
        this.estado = e.estado;
        this.principal = e.principal;
        this.monitorador = e.monitorador;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Monitorador getMonitorador() {
        return monitorador;
    }

    public void setMonitorador(Monitorador monitorador) {
        this.monitorador = monitorador;
    }

    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", principal='" + principal + '\'' +
                ", monitorador=" + monitorador.getNome() +
                '}';
    }

    public String getNomeOrRazao() {
        if (monitorador.getTipo() == TipoPessoa.FISICA)
            return monitorador.getNome();
        else
            return monitorador.getRazao();
    }
}

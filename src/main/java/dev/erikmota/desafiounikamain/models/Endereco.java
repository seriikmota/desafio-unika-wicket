package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Endereco implements Serializable, Comparable<Endereco> {
    private Long id;
    private String endereco;
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private Boolean principal;
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

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }


    public void setMonitorador(Monitorador monitorador){
        this.monitorador = monitorador;
    }

    public Monitorador getMonitorador() {
        return monitorador;
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
                '}';
    }

    @Override
    public int compareTo(Endereco e) {
        int comparacaoEstado = this.estado.compareTo(e.estado);
        if (comparacaoEstado != 0) {
            return comparacaoEstado;
        }
        int comparacaoCidade = this.cidade.compareTo(e.cidade);
        if (comparacaoCidade != 0) {
            return comparacaoCidade;
        }
        return this.endereco.compareTo(e.endereco);
    }
}

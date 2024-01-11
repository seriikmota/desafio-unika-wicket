package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Monitorador implements Serializable, Comparable<Monitorador>{
    private Long id;
    private TipoPessoa tipo;
    private String cpf;
    private String cnpj;
    private String nome;
    private String razao;
    private String rg;
    private String inscricao;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
    private Boolean ativo;
    private List<Endereco> enderecos;

    public Monitorador(){

    }
    public Monitorador(Monitorador m) {
        this.id = m.id;
        this.tipo = m.tipo;
        this.cpf = m.cpf;
        this.cnpj = m.cnpj;
        this.nome = m.nome;
        this.razao = m.razao;
        this.email = m.email;
        this.rg = m.rg;
        this.inscricao = m.inscricao;
        this.data = m.data;
        this.ativo = m.ativo;
        this.enderecos = m.enderecos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPessoa getTipo() {
        return tipo;
    }

    public void setTipo(TipoPessoa tipo) {
        this.tipo = tipo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getInscricao() {
        return inscricao;
    }

    public void setInscricao(String inscricao) {
        this.inscricao = inscricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    @Override
    public String toString() {
        return "Monitorador{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", cpf='" + cpf + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nome='" + nome + '\'' +
                ", razao='" + razao + '\'' +
                ", email='" + email + '\'' +
                ", rg='" + rg + '\'' +
                ", inscricao='" + inscricao + '\'' +
                ", data='" + data + '\'' +
                ", ativo=" + ativo +
                ", enderecos=" + enderecos +
                '}';
    }

    @Override
    public int compareTo(Monitorador m) {
        int comparacaoTipo = this.tipo.compareTo(m.tipo);
        if (comparacaoTipo != 0) {
            return comparacaoTipo;
        }
        if (this.tipo == TipoPessoa.FISICA) {
            return this.nome.compareTo(m.nome);
        } else {
            return this.razao.compareTo(m.razao);
        }
    }
}
package com.hiaggo.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import javax.validation.constraints.NotEmpty;

@Entity
@Table
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O 'Nome' deve ser informado.")
    @Column(name = "NOME")
    private String nome;

    @NotEmpty(message = "O 'CNPJ' deve ser informado.")
    @Column(name = "CNPJ")
    private String cnpj;

    @NotEmpty(message = "A 'Cidade' deve ser informada.")
    @Column(name = "CIDADE")
    private String cidade;

    public Fornecedor() {

    }

    public Fornecedor(String nome, String cnpj, String cidade) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.cidade = cidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCNPJ() {
        return cnpj;
    }

    public void setCNPJ(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @OneToMany(mappedBy="fornecedor")
    private List<Produto> produtos = new ArrayList<Produto>();
    
    @Override
    public String toString() {
        return nome;
    }
}

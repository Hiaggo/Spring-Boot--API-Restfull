package com.hiaggo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fornecedor")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O 'Nome' deve ser informado.")
    private String nome;

    @NotEmpty(message = "A 'Categoria' deve ser informada.")
    @Column(name = "CATEGORIA_PRODUTO")
    private String categoriaProduto;

    @NotEmpty(message = "A 'Descrição' deve ser informada.")
    @Column(name = "DESCRICAO_PRODUTO")
    private String descricaoProduto;

    @ManyToOne
    @JoinColumn(name = "FORNECEDOR_ID")
    @NotNull(message = "O 'Fornecedor' deve ser informado.")
    private Fornecedor fornecedor;

    public Produto(String nome, String categoriaProduto, String descricaoProduto, Fornecedor fornecedor) {
        this.nome = nome;
        this.categoriaProduto = categoriaProduto;
        this.descricaoProduto = descricaoProduto;
        this.fornecedor = fornecedor;
    }
}

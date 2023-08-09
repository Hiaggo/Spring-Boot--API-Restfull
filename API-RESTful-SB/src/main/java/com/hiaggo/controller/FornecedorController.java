package com.hiaggo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiaggo.model.Fornecedor;
import com.hiaggo.model.Produto;
import com.hiaggo.service.FornecedorService;

import java.util.List;

@RestController
@RequestMapping("fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @PostMapping
    public Fornecedor cadastrarFornecedor(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.cadastrarFornecedor(fornecedor);
    }

    @PutMapping
    public Fornecedor atualizarFornecedor(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.atualizarFornecedor(fornecedor);
    }

    @DeleteMapping("{idFornecedor}")    // http://localhost:8081/fornecedores/5
    public void removerFornecedor(@PathVariable("idFornecedor") Long id) {
        fornecedorService.removerFornecedor(id);
    }

    @GetMapping
    public List<Fornecedor> recuperarFornecedores() {
        return fornecedorService.recuperarFornecedores();
    }

    @GetMapping("{idFornecedor}")
    private Fornecedor recuperarFornecedorPorId(@PathVariable("idFornecedor") Long id) {
        return fornecedorService.recuperarFornecedorPorId(id);
    }
}

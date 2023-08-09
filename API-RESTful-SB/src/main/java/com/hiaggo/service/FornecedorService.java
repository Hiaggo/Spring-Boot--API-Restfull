package com.hiaggo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hiaggo.exception.EntidadeNaoEncontradaException;
import com.hiaggo.model.Fornecedor;
import com.hiaggo.repository.FornecedorRepository;

import java.util.List;

@Service
public class FornecedorService {
    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Fornecedor cadastrarFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor atualizarFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public void removerFornecedor(Long id) {
        recuperarFornecedorPorId(id);
        fornecedorRepository.deleteById(id);
    }

    public List<Fornecedor> recuperarFornecedores() {
        return fornecedorRepository.findAll(Sort.by("id"));
    }

    public Fornecedor recuperarFornecedorPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Fornecedor número " + id + " não encontrada."));
    }
}

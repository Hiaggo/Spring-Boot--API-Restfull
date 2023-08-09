package com.hiaggo.controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import com.hiaggo.exception.EntidadeNaoEncontradaException;
import com.hiaggo.model.Produto;
import com.hiaggo.service.ProdutoService;

import java.util.List;

@RestController
@RequestMapping(path ="produtos")    // http://localhost:8081/produtos
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> recuperarProdutos() {
        return produtoService.recuperarProdutos();
    }

    @PostMapping
    public Produto cadastrarProduto(@RequestBody Produto produto) {
        return produtoService.cadastrarProduto(produto);
    }

//    @GetMapping("{idProduto}")                // http://localhost:8081/produtos/1
//    public ResponseEntity<?> recuperarProdutoPorId(@PathVariable("idProduto") Long id) {
//        try {
//            Produto produto = produtoService.recuperarProdutoPorId(id);
//            return new ResponseEntity<>(produto, HttpStatus.OK);
//        }
//        catch(EntidadeNaoEncontradaException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("{idProduto}")     // http://localhost:8081/produtos/5
    public Produto recuperarProdutoPorId(@PathVariable("idProduto") Long id) {
        return produtoService.recuperarProdutoPorId(id);
    }

    @PutMapping
    public Produto atualizarProduto(@RequestBody Produto produto) {
        return produtoService.atualizarProduto(produto);
    }

    @DeleteMapping("{idProduto}")    // http://localhost:8081/produtos/5
    public void removerProduto(@PathVariable("idProduto") Long id) {
        produtoService.removerProduto(id);
    }

    @GetMapping("fornecedor/{idFornecedor}")// http://localhost:8081/produtos/fornecedor/1
    public List<Produto> recuperarProdutosPorFornecedorV1(@PathVariable("idFornecedor") Long id) {
        return produtoService.recuperarProdutosPorFornecedor(id);
    }

    @GetMapping("fornecedor")// http://localhost:8081/produtos/fornecedor?idFornecedor=1
    public List<Produto> recuperarProdutosPorFornecedorV2(@RequestParam("idFornecedor") Long id) {
        return produtoService.recuperarProdutosPorFornecedor(id);
    }
}
           // http://localhost:8081/ap1/produtos/1
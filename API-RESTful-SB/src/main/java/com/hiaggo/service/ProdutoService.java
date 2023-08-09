package com.hiaggo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiaggo.exception.EntidadeNaoEncontradaException;
import com.hiaggo.model.Produto;
import com.hiaggo.repository.FornecedorRepository;
import com.hiaggo.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;
    public List<Produto> recuperarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto recuperarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Produto número " + id + " não encontrado."));
    }

    // O método atualizarProduto() abaixo, recebe um objeto destacado (produto), recupera um
    // objeto persistente (umProduto) do banco de dados e efetua o merge(). Note que o save
    // recebe uma entidade do tipo S e retornar um objeto do tipo S. Isto é, recebe o objeto
    // produto e retorna o objeto umProduto após ocorrer o merge.
    public Produto atualizarProduto(Produto produto) {
        // Somente com essa linha de código: return produtoRepository.save(produto);
        // Se não mudarmos a fornecedor serão executados esses 2 comandos:
        //    1. select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from produto p1_0 left join fornecedor c1_0 on c1_0.id=p1_0.fornecedor_id where p1_0.id=?
        //    2. update produto set fornecedor_id=?, data_cadastro=?, nome=?, preco=? where id=?
        // E se mudarmos a fornecedor serão executados esses 3 comandos:
        //    1. select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from produto p1_0 left join fornecedor c1_0 on c1_0.id=p1_0.fornecedor_id where p1_0.id=?
        //    2. select c1_0.id,c1_0.nome,c1_0.slug from fornecedor c1_0 where c1_0.id=?   (RECUPERA A NOVA CATEGORIA)
        //    3. update produto set fornecedor_id=?, data_cadastro=?, nome=?, preco=? where id=?

        // E com o código que escrevemos abaixo será EXATAMENTE IGUAL.

        // Se não mudarmos a fornecedor, o código abaixo executará esses 2 comandos:
        //    1. Para recuperarProdutoPorId(): select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from produto p1_0 left join fornecedor c1_0 on c1_0.id=p1_0.fornecedor_id where p1_0.id=?
        //    2. E para o método save(): update produto set fornecedor_id=?, data_cadastro=?, nome=?, preco=? where id=?

        // E se mudarmos a fornecedor:
        // Sem o findById() abaixo esse método executaria esses 3 comandos:
        //    1. Para recuperarProdutoPorId(): select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from produto p1_0 left join fornecedor c1_0 on c1_0.id=p1_0.fornecedor_id where p1_0.id=?
        // E para o método save(), os 2 abaixo:
        //    2. Para recuperar a nova fornecedor: select c1_0.id,c1_0.nome,c1_0.slug from fornecedor c1_0 where c1_0.id=?
        //    3. Para atualizar o produto: update produto set fornecedor_id=?, data_cadastro=?, nome=?, preco=? where id=?

        // E se acrescentarmos o findById() abaixo, então ficará assim:
        //    1. Para recuperarProdutoPorId(): select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco
        //                                     from produto p1_0 left join fornecedor c1_0 on c1_0.id=p1_0.fornecedor_id where p1_0.id=?
        //    2. Para o findById() que recupera a nova fornecedor: select c1_0.id,c1_0.nome,c1_0.slug from fornecedor c1_0 where c1_0.id=?
        //    3. E para o save() que atualiza o produto: update produto set fornecedor_id=?, data_cadastro=?, nome=?, preco=? where id=?

        Produto umProduto = recuperarProdutoPorId(produto.getId());
        if (!(produto.getFornecedor().getId().equals(umProduto.getFornecedor().getId()))) {
            fornecedorRepository.findById(produto.getFornecedor().getId())
                    .orElseThrow(()-> new EntidadeNaoEncontradaException(
                            "Fornecedor número " + produto.getFornecedor().getId() + " não encontrada."));
        }
        return produtoRepository.save(produto);
    }

    public void removerProduto(Long id) {
        recuperarProdutoPorId(id);
        produtoRepository.deleteById(id);
    }

    public List<Produto> recuperarProdutosPorFornecedor(Long id) {
        return produtoRepository.findByFornecedorId(id);
    }
}

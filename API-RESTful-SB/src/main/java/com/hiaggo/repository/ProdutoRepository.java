package com.hiaggo.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hiaggo.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id = :id")
    Optional<Produto> recuperarProdutoPorIdComLock(@Param("id") Long id);

    @Query("select p from Produto p left join fetch p.fornecedor where p.fornecedor.id = :id")
    List<Produto> findByFornecedorId(Long id);
}

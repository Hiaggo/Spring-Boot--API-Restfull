package com.hiaggo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hiaggo.model.Fornecedor;
import com.hiaggo.model.Produto;
import com.hiaggo.repository.FornecedorRepository;
import com.hiaggo.repository.ProdutoRepository;


@SpringBootApplication
public class ResTful01SbApplication implements CommandLineRunner {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ResTful01SbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Fornecedor fornecedor1 = new Fornecedor("3M do Brasil", "45.985.371/0001-08", "Sumaré");
		fornecedorRepository.save(fornecedor1);

		Fornecedor fornecedor2 = new Fornecedor("Da Terrinha", "15.074.376/0001-18", "São Paulo");
		fornecedorRepository.save(fornecedor2);

		Produto produto1 = new Produto(
			"Luva de Limpeza",
			"Limpeza",
			"Luva de Limpeza Scotch-Brite",
			fornecedor1
		);
		produtoRepository.save(produto1);

		Produto produto2 = new Produto(
			"Vassoura",
			"Limpeza",
			"Vassoura Scotch-Brite",
			fornecedor1
		);
		produtoRepository.save(produto2);

		Produto produto3 = new Produto(
			"Tapioca",
			"Alimentação",
			"Tapioca Da Terrinha",
			fornecedor2
		);
		produtoRepository.save(produto3);
	}
}

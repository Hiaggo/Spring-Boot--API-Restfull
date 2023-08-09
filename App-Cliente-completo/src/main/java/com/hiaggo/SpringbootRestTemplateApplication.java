package com.hiaggo;



import com.hiaggo.exception.EntidadeNaoEncontradaException;
import com.hiaggo.exception.ErrorHandler;
import com.hiaggo.exception.ViolacaoDeConstraintException;
import com.hiaggo.modelo.Fornecedor;
import com.hiaggo.modelo.Produto;
import corejava.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


public class SpringbootRestTemplateApplication {
    // Nesta aplicação cliente estamos utilizando o Log4j2 (VERSÃO 2) para evitar que sejam
    // exibidas informações de DEBUG na console emitidas pelos métodos de RestTemplate do
    // framework Spring. Daí ter sido definido, no arquivo log4j2.xml, um logger Root para
    // o nível info. Os víveis de log são: trace < debug < info < warn < error < fatal

    private static Logger logger = LoggerFactory.getLogger(SpringbootRestTemplateApplication.class);
    // private static Logger rootLogger =  LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    // Criando o objeto RestTemplate
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {

        // Designando um ErrorHandler ao objeto restTemplate
        restTemplate.setErrorHandler(new ErrorHandler());

        logger.info("Iniciando a execução da aplicação cliente.");
        // rootLogger.info("Iniciando a execução da aplicação cliente utilizando o root logger.");

        String nomeProduto;
        String categoriaProduto;
        String descricaoProduto;
        String nomeFornecedor;
        String cnpjFornecedor;
        String cidadeFornecedor;
        Produto produto = null;
        Fornecedor fornecedor = null;

        boolean continua = true;
        while (continua) {
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Cadastrar um produto");
            System.out.println("2. Alterar um produto");
            System.out.println("3. Remover um produto");
            System.out.println("4. Recuperar um produto");
            System.out.println("5. Listar todos os produtos");
            System.out.println("6. Listar todos os produtos por fornecedor (usando request param)");
            System.out.println("7. Cadastar um fornecedor");
            System.out.println("8. Alterar um fornecedor");
            System.out.println("9. Remover um fornecedor");
            System.out.println("10. Recuperar um fornecedor");
            System.out.println("11. Listar todos os fornecedores");
            System.out.println("12. Sair");

            // Métodos utilizados:
            // exchange
            // getForObject
            // postForEntity
            // put
            // delete

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 12:");

            switch (opcao) {
                case 1 -> {
                    nomeProduto = Console.readLine('\n' + "Informe o nome do produto: ");
                    categoriaProduto = Console.readLine('\n' + "Informe a categoria do produto: ");
                    descricaoProduto = Console.readLine('\n' + "Informe a descrição do produto: ");

                    long id = Console.readInt('\n' + "Informe o número do fornecedor: ");
                    try {
                        // Recuperando a fornecedor do produto
                        ResponseEntity<Fornecedor> res = restTemplate.exchange(
                            "http://localhost:8081/fornecedores/{id}",
                            HttpMethod.GET, null, Fornecedor.class, id
                        );
                        fornecedor = res.getBody();
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' + "Fornecedor: " + fornecedor.getNome());

                    produto = new Produto(
                        nomeProduto, 
                        categoriaProduto,
                        descricaoProduto,
                        fornecedor
                    );

                    // O método exchange abaixo envia uma requisição HTTP para o endpoint fornecido.
                    // No corpo da requisição (objeto requestEntity) é enviado um objeto do tipo produto.
                    // Caso seja necessário enviar algum header, deverá ser enviado como segundo argumento
                    // do construtor de HttpEntity. O método exchange retorna um objeto do tipo ResponseEntity
                    // que, por default, retorna o Status http (HttpStatus) 200 OK.

                    // Ao contrário do Postman, que exige um header do tipo Content-type valendo application/json
                    // para que seja enviado ao servidor conteúdo json incluído no corpo da requisição http,
                    // a API restTemplate já assume, por default, que será enviado json. Daí não ser necessário
                    // o envio de um header do tipo Content-type.

                    try {
                        // HttpHeaders httpHeaders = new HttpHeaders();
                        // httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        // httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                        // HttpEntity<Produto> requestEntity = new HttpEntity<>(umProduto, httpHeaders);

                        // Enviando uma requisição do tipo POST ao servidor para cadastrar um produto
//                        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
//                        ResponseEntity<Produto> res = restTemplate.exchange(
//                                "http://localhost:8081/produtos",
//                                HttpMethod.POST, requestEntity, Produto.class);
//                        produto = res.getBody();

                        ResponseEntity<Produto> res = restTemplate.postForEntity(
                                "http://localhost:8081/produtos", produto, Produto.class);
                        produto = res.getBody();

                        System.out.println('\n' + "Produto número " + produto.getId() + " cadastrado com sucesso!");

                        System.out.println('\n' +
                            "Id = " + produto.getId() +
                            ";  Nome = " + produto.getNome() +
                            ";  Categoria = " + produto.getCategoriaProduto() +
                            ";  Descrição = " + produto.getDescricaoProduto() +
                            ";  Fornecedor = " + produto.getFornecedor().getNome()
                        );
                    } catch (ViolacaoDeConstraintException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        // Recuperando o produto que se deseja alterar
                        produto = recuperarObjeto(
                                "Informe o número do produto que você deseja alterar: ",
                                "http://localhost:8081/produtos/{id}", Produto.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }
                    catch (Exception e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                        "Id = " + produto.getId() +
                        ";  Nome = " + produto.getNome() +
                        ";  Categoria = " + produto.getCategoriaProduto() +
                        ";  Descrição = " + produto.getDescricaoProduto() +
                        ";  Fornecedor = " + produto.getFornecedor().getNome()
                    );

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println("1. Nome");
                    System.out.println("2. Categoria");
                    System.out.println("3. Descrição");
                    System.out.println("4. Fornecedor");

                    int opcaoAlteracao = Console.readInt('\n' + "Digite o número 1, 2, 3 ou 4:");

                    if (opcaoAlteracao == 1) {
                        String novoNome = Console.readLine("Digite o novo nome: ");
                        produto.setNome(novoNome);
                    } else if (opcaoAlteracao == 2) {
                        String novaCategoria = Console.readLine("Digite a nova categoria : ");
                        produto.setCategoriaProduto(novaCategoria);
                    } else if (opcaoAlteracao == 3) {
                        String novaDescricao = Console.readLine("Digite a nova descrição: ");
                        produto.setDescricaoProduto(novaDescricao);
                    } else if (opcaoAlteracao == 4) {
                        try {
                            fornecedor = recuperarObjeto(
                                    "Informe o número do novo fornecedor: ",
                                    "http://localhost:8081/fornecedores/{id}", Fornecedor.class);
                            produto.setFornecedor(fornecedor);
                        }
                        catch (EntidadeNaoEncontradaException e) {
                            System.out.println('\n' + e.getMessage());
                            break;
                        }
                    } else {
                        System.out.println("\nOpção inválida");
                        break;
                    }

                    try {
                        // Enviando uma requisição do tipo PUT para o servidor para atualizar o produto
//                        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
//                        ResponseEntity<Produto> res = restTemplate.exchange(
//                                "http://localhost:8081/produtos",
//                                HttpMethod.PUT, requestEntity, Produto.class);
//                        produto = res.getBody();

                        restTemplate.put("http://localhost:8081/produtos", produto);

                        System.out.println('\n' + "Produto número " + produto.getId() + " alterado com sucesso!");

                        System.out.println('\n' +
                            "Id = " + produto.getId() +
                            ";  Nome = " + produto.getNome() +
                            ";  Categoria = " + produto.getCategoriaProduto() +
                            ";  Descrição = " + produto.getDescricaoProduto() +
                            ";  Fornecedor = " + produto.getFornecedor().getNome()
                        );
                    } catch (ViolacaoDeConstraintException | EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        // Recuperando o produto que se deseja remover
                        produto = recuperarObjeto(
                                "Informe o número do produto que você deseja remover: ",
                                "http://localhost:8081/produtos/{id}", Produto.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                        "Id = " + produto.getId() +
                        ";  Nome = " + produto.getNome() +
                        ";  Categoria = " + produto.getCategoriaProduto() +
                        ";  Descrição = " + produto.getDescricaoProduto() +
                        ";  Fornecedor = " + produto.getFornecedor().getNome()
                    );

                    String resp = Console.readLine('\n' +
                            "Confirma a remoção do produto?");

                    if (resp.equals("s")) {
                        try {
                            // Enviando uma requisição do tipo DELETE para remover o produto
                            // restTemplate.exchange(
                            // "http://localhost:8081/produtos/{id}",
                            // HttpMethod.DELETE, null, Produto.class, produto.getId());
                            restTemplate.delete("http://localhost:8081/produtos/{id}", produto.getId());

                            System.out.println('\n' + "Produto número " + produto.getId() + " removido com sucesso!");
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println('\n' + e.getMessage());
                            break;
                        }
                        // Onde aparece null, poderia ter sido  especificado um objeto do tipo HttpEntity.
                        // Como nesta requisição http não está sendo necessário enviar nada no corpo da
                        // requisição e nem será preciso enviar nenhum header, o objeto HttpEntity não é
                        // necessário. Já o argumento Produto.class indica o tipo do objeto que será retornado
                        // na resposta http.
                    } else {
                        System.out.println('\n' + "Produto não removido.");
                    }

                }
                case 4 -> {
                    try {
                        produto = recuperarObjeto(
                            "Informe o número do produto que você deseja recuperar: ",
                            "http://localhost:8081/produtos/{id}", Produto.class
                        );
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }
                    catch (Exception e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                        "Id = " + produto.getId() +
                        ";  Nome = " + produto.getNome() +
                        ";  Categoria = " + produto.getCategoriaProduto() +
                        ";  Descrição = " + produto.getDescricaoProduto() +
                        ";  Fornecedor = " + produto.getFornecedor().getNome()
                    );
                }
                case 5 -> {
                    // Enviando uma requisição do tipo GET para recuperar todos os produtos
                    ResponseEntity<Produto[]> res = restTemplate.exchange(
                            "http://localhost:8081/produtos",
                            HttpMethod.GET, null, Produto[].class);
                    Produto[] produtos = res.getBody();

                    for (Produto umProduto : produtos) {
                        System.out.println('\n' +
                            "Id = " + umProduto.getId() +
                            ";  Nome = " + umProduto.getNome() +
                            ";  Categoria = " + umProduto.getCategoriaProduto() +
                            ";  Descrição = " + umProduto.getDescricaoProduto() +
                            ";  Fornecedor = " + umProduto.getFornecedor().getNome()
                        );
                    }
                }
                case 6 -> {
                    long id = Console.readInt('\n' + "Informe o número do fornecedor: ");

                    // Enviando uma requisição do tipo GET para recuperar todos os produtos
                    // de um determinado fornecedor (utilizando parâmetro de requisição)
                    ResponseEntity<Produto[]> res = restTemplate.exchange(
                            "http://localhost:8081/produtos/fornecedor?idFornecedor=" + id,
                            HttpMethod.GET, null, Produto[].class);
                    Produto[] produtos = res.getBody();

                    if (produtos.length == 0) {
                        System.out.println("\nNenhum produto foi encontrado para esta fornecedor.");
                        break;
                    }

                    for (Produto umProduto : produtos) {
                        System.out.println('\n' +
                            "Id = " + umProduto.getId() +
                            "  Nome = " + umProduto.getNome() +
                            "  Categoria = " + umProduto.getCategoriaProduto() +
                            "  Descrição = " + umProduto.getDescricaoProduto() +
                            "  Fornecedor = " + umProduto.getFornecedor().getNome()
                        );
                    }

                }
                case 7 -> {
                    // Enviando uma requisição do tipo POST para salvar um novo fornecedor
                    nomeFornecedor = Console.readLine('\n' + "Informe o nome do fornecedor: ");
                    cnpjFornecedor = Console.readLine('\n' + "Informe o CNPJ do fornecedor: ");
                    cidadeFornecedor = Console.readLine('\n' + "Informe a cidade de origem do fornecedor: ");

                    fornecedor = new Fornecedor(
                        nomeFornecedor, 
                        cnpjFornecedor,
                        cidadeFornecedor
                    );

                    try {
                        ResponseEntity<Fornecedor> res = restTemplate.postForEntity(
                                "http://localhost:8081/fornecedores", fornecedor, Fornecedor.class);
                        fornecedor = res.getBody();

                        System.out.println('\n' + "Fornecedor número " + fornecedor.getId() + " cadastrado com sucesso!");

                        System.out.println('\n' +
                            "Id = " + fornecedor.getId() +
                            ";  Nome = " + fornecedor.getNome() +
                            ";  CNPJ = " + fornecedor.getCNPJ() +
                            ";  Cidade = " + fornecedor.getCidade()
                        );
                    } catch (ViolacaoDeConstraintException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 8 -> {
                    // Enviando uma requisição do tipo PUT para alterar um fornecedor
                    try {
                        // Recuperando o fornecedor que se deseja alterar
                        fornecedor = recuperarObjeto(
                                "Informe o número do fornecedor que você deseja alterar: ",
                                "http://localhost:8081/fornecedores/{id}", Fornecedor.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                        "Id = " + fornecedor.getId() +
                        ";  Nome = " + fornecedor.getNome() +
                        ";  CNPJ = " + fornecedor.getCNPJ() +
                        ";  Cidade = " + fornecedor.getCidade()
                    );

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. Nome");
                    System.out.println("2. CNPJ");
                    System.out.println("3. Cidade");

                    int opcaoAlteracao = Console.readInt('\n' + "Digite o número 1, 2 ou 3:");

                    if (opcaoAlteracao == 1) {
                        String novoNome = Console.readLine("Digite o novo nome: ");
                        fornecedor.setNome(novoNome);
                    } else if (opcaoAlteracao == 2) {
                        String novoCNPJ = Console.readLine("Digite o novo CNPJ : ");
                        fornecedor.setCNPJ(novoCNPJ);
                    } else if (opcaoAlteracao == 3) {
                        String novaCidade = Console.readLine("Digite o novo cidade: ");
                        fornecedor.setCidade(novaCidade);
                    } else {
                        System.out.println("\nOpção inválida");
                        break;
                    }

                    try {
                        restTemplate.put("http://localhost:8081/fornecedores", fornecedor);

                        System.out.println('\n' + "Fornecedor número " + fornecedor.getId() + " alterado com sucesso!");

                        System.out.println('\n' +
                            "Id = " + fornecedor.getId() +
                            ";  Nome = " + fornecedor.getNome() +
                            ";  CNPJ = " + fornecedor.getCNPJ() +
                            ";  Cidade = " + fornecedor.getCidade()
                        );
                    } catch (ViolacaoDeConstraintException | EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 9 -> {
                    // Enviando uma requisição do tipo DELETE para remover um fornecedor
                    try {
                        fornecedor = recuperarObjeto(
                                "Informe o número do fornecedor que você deseja remover: ",
                                "http://localhost:8081/fornecedores/{id}", Fornecedor.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                    }

                    System.out.println('\n' +
                        "Id = " + fornecedor.getId() +
                        ";  Nome = " + fornecedor.getNome() +
                        ";  CNPJ = " + fornecedor.getCNPJ() +
                        ";  Cidade = " + fornecedor.getCidade()
                    );

                    String resp = Console.readLine('\n' + "Confirma a remoção do fornecedor?");

                    if (resp.equals("s")) {
                        try {
                            restTemplate.delete("http://localhost:8081/fornecedores/{id}", fornecedor.getId());

                            System.out.println('\n' + "Fornecedor número " + fornecedor.getId() + " removido com sucesso!");
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println('\n' + e.getMessage());
                        } catch (ResourceAccessException e) {
                            System.out.println('\n' + "Fornecedor possui produtos associados. Remova-os primeiro.");
                        }
                    } else {
                        System.out.println('\n' + "Produto não removido.");
                    }
                }
                case 10 -> {
                    // Enviando uma requisição do tipo GET para recuperar um fornecedor
                    try {
                        fornecedor = recuperarObjeto(
                                "Informe o número do fornecedor que você deseja recuperar: ",
                                "http://localhost:8081/fornecedores/{id}", Fornecedor.class);

                        System.out.println('\n' +
                            "Id = " + fornecedor.getId() +
                            ";  Nome = " + fornecedor.getNome() +
                            ";  CNPJ = " + fornecedor.getCNPJ() +
                            ";  Cidade = " + fornecedor.getCidade()
                        );
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 11 -> {
                    // Enviando uma requisição do tipo GET para recuperar todos os fornecedores
                    ResponseEntity<Fornecedor[]> res = restTemplate.exchange(
                            "http://localhost:8081/fornecedores",
                            HttpMethod.GET, null, Fornecedor[].class);
                    Fornecedor[] fornecedores = res.getBody();

                    for (Fornecedor umFornecedor : fornecedores) {
                        System.out.println('\n' +
                            "Id = " + umFornecedor.getId() +
                            ";  Nome = " + umFornecedor.getNome() +
                            ";  CNPJ = " + umFornecedor.getCNPJ() +
                            ";  Cidade = " + umFornecedor.getCidade()
                        );
                    }
                }
                case 12 -> {
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }

    private static <T> T recuperarObjeto(String msg, String url, Class<T> classe) {
        int id = Console.readInt('\n' + msg);
        return restTemplate.getForObject(url, classe, id);
    }
}

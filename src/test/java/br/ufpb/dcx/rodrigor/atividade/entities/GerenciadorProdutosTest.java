package br.ufpb.dcx.rodrigor.atividade.entities;

import br.ufpb.dcx.rodrigor.atividade.exceptions.GerenciadorProdutosException;
import br.ufpb.dcx.rodrigor.atividade.exceptions.ProdutoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorProdutosTest {

    GerenciadorProdutosInterface gerenciador;
    Produto computador;
    Produto celular;


    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorProdutos();

        try{
            computador = new Produto("Computador", "1236DE", 3500.00, 10);
        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }

        try{
            celular= new Produto("Celular", "7836CE", 2000.00, 20);
        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }

    }

    @Test
    void cadastrarProduto() {
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());
            assertTrue(gerenciador.cadastrarProduto(celular));
        } catch (GerenciadorProdutosException e) {
            fail("Erro, problema com a função cadastrarProduto()");
        }

        //teste cadastrando um produto com a mesma SKU de um produto já cadastrado no PDV
        try {
            Produto televisao = new Produto("Televisão", "1236DE", 1500.00, 12);

            try {
                gerenciador.cadastrarProduto(televisao);
                fail("Erro, foi cadastrado um produto com SKU igual ao de outro produto do PDV");
            } catch (GerenciadorProdutosException e) {
                assertEquals("A SKU informado já está cadastrado no sistema e relacionada a um produto diferente do informado", e.getMessage());
            }

        } catch (ProdutoException e){
            fail("Erro ao instanciar produto");
        }

        //teste cadastrando um produto com o mesmo nome e a mesma SKU de um produto já cadastrado no PDV
        try {
            Produto p = new Produto("Celular", "7836CE", 1250.00, 22);

            try {
                gerenciador.cadastrarProduto(p);
                fail("Erro, foi cadastrado um produto com o nome e a SKU igual ao de outro produto do PDV");
            } catch (GerenciadorProdutosException e) {
                assertEquals("O produto informado já está cadastrado no sistema", e.getMessage());
            }

        } catch (ProdutoException e){
            fail("Erro ao instanciar produto");
        }

    }

    @Test
    void removerProduto() {
        //cadastrando um produto para testar o removerProduto()
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());
        } catch (GerenciadorProdutosException e) {
            fail("Erro, problema com a função cadastrarProduto()");
        }

        //teste remover o produto cadastrado
        try {
            gerenciador.removerProduto(computador.getNome());
            assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        } catch (GerenciadorProdutosException e) {
            fail("Erro ao remover o produto");
        }

        //teste removendo um produto da lista de produtos cadastrados, sendo que a lista de produtos está vazia
        try {
            gerenciador.removerProduto(celular.getNome());
            fail("Erro. problema com a função remover. a função ta removendo o produto sendo que a lista de produtos está vazia");
        } catch (GerenciadorProdutosException e) {
            assertEquals("Não existe produtos cadastrados", e.getMessage());
        }

        //teste removendo um produto que não está na lista de produtos cadastrados, sendo que a lista é diferente de vazia
        try {
            Produto p = new Produto("Iphone", "5213ER", 4000.00, 10);

            assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));

            try {
                assertTrue(gerenciador.cadastrarProduto(computador));
                assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());
                assertTrue(gerenciador.cadastrarProduto(celular));
            } catch (GerenciadorProdutosException e) {
                fail("Erro, problema com a função cadastrarProduto()");
            }

            try {
                gerenciador.removerProduto(p.getNome());
                fail("Erro. problema com a função remover. a função ta removendo um produto que não está na lista");
            } catch (GerenciadorProdutosException e) {
                assertEquals("Não foi possivel remover o produto pois, o produto Iphone informado não se encontra na lista de produtos", e.getMessage());
            }

        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }

    }

    @Test
    void getProdutoNome() {
        //testando pegar um produto da lista de produstos pelo seu nome, sendo que a lista está vazia
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            gerenciador.getProdutoNome("Geladeira");
            fail("Erro com a função getProdutoNome(), pois ela conseguiu retornar um produto sendo que a lista de produtos está vazia");
        } catch (GerenciadorProdutosException e){
            assertEquals("Não existe produtos cadastrados", e.getMessage());
        }

        //testando pegar um produto que foi cadastrado na lista de produtos pelo seu nome
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals(computador, gerenciador.getProdutoNome("Computador"));
        } catch (GerenciadorProdutosException e){
            fail("Erro com a função getProdutoNome(), pois está retornando um produto diferente do esperado quando informado o nome do produto");
        }

        assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());

        try {
            gerenciador.cadastrarProduto(celular);
            assertEquals(celular, gerenciador.getProdutoNome("Celular"));
        } catch (GerenciadorProdutosException e){
            fail("Erro com a função getProdutoNome(), pois está retornando um produto diferente do esperado quando informado o nome do produto");
        }

        //testando pegar um produto que não foi cadastrado na lista de produtos, lista é diferente de vazio
        try {
            Produto p = new Produto("Iphone", "5213ER", 4000.00, 10);
            assertFalse(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));

            try{
                assertEquals(null, gerenciador.getProdutoNome("Iphone"));
            } catch (GerenciadorProdutosException e){
                fail("Erro com a função getProdutoNome(), a função está pegando um produto que não existe na lista de produtos cadastardos");
            }

        } catch (ProdutoException e){
            fail("Erro ao instanciar produto");
        }

    }

    @Test
    void getProdutoSku() {
        //testando pegar um produto da lista de produstos pelo seu SKU, sendo que a lista está vazia
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            gerenciador.getProdutoSku("7836CE");
            fail("Erro com a função getProdutoSku(), pois ela conseguiu retornar um produto sendo que a lista de produtos está vazia");
        } catch (GerenciadorProdutosException e){
            assertEquals("Não existe produtos cadastrados", e.getMessage());
        }

        //testando pegar um produto que foi cadastrado na lista de produtos pelo seu SKU
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals(computador, gerenciador.getProdutoSku("1236DE"));
        } catch (GerenciadorProdutosException e){
            fail("Erro com a função getProdutoSku(), pois está retornando um produto diferente do esperado quando informado o SKU do produto");
        }

        assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());

        try {
            gerenciador.cadastrarProduto(celular);
            assertEquals(celular, gerenciador.getProdutoSku("7836CE"));
        } catch (GerenciadorProdutosException e){
            fail("Erro com a função getProdutoSku(), pois está retornando um produto diferente do esperado quando informado o SKU do produto");
        }

        //testando pegar um produto que não foi cadastrado na lista de produtos, lista é diferente de vazio
        try {
            Produto p = new Produto("Iphone", "5213ER", 4000.00, 10);
            assertFalse(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));

            try{
                assertEquals(null, gerenciador.getProdutoSku("5213ER"));
            } catch (GerenciadorProdutosException e){
                fail("Erro com a função getProdutoSku(), a função está pegando um produto que não existe na lista de produtos cadastardos");
            }

        } catch (ProdutoException e){
            fail("Erro ao instanciar produto");
        }

    }

    @Test
    void listarProdutos() {
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());
            assertTrue(gerenciador.cadastrarProduto(celular));
            gerenciador.removerProduto(computador.getNome());
            assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Celular\n\tSku: 7836CE\n\tValor: 2000.0\n\tQuantidade: 20\n\n", gerenciador.listarProdutos());
        } catch (GerenciadorProdutosException e) {
            fail("Erro, problema com a função cadastrarProduto()");
        }
    }

    @Test
    void pesquisaProduto() {
        //testando a função pesquisaProduto() sendo que a lista de produtos cadastrados está vazia
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            gerenciador.pesquisaProduto("1236DE");
            fail("Erro, foi encontrado/retornadoo um produto, sendo que a lista de produtos cadastrados é vazia");
        } catch (GerenciadorProdutosException e) {
            assertEquals("Não existe produtos cadastrados", e.getMessage());
        }

        //testando a função pesquisaProduto() com um produto existente e presente na lista de produtos cadastrados
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());
            assertEquals("\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.pesquisaProduto("1236DE"));
            assertTrue(gerenciador.cadastrarProduto(celular));
            assertEquals("\tProduto: Celular\n\tSku: 7836CE\n\tValor: 2000.0\n\tQuantidade: 20\n\n", gerenciador.pesquisaProduto("7836CE"));
        } catch (GerenciadorProdutosException e) {
            fail("Erro, problema com a função cadastrarProduto()");
        }

        //testando a função pesquisaProduto() com um produto que não existe na lista de produtos cadastrados
        assertFalse(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            Produto p = new Produto("Televisão", "9856TE", 1400.00, 25);

            try {
                gerenciador.pesquisaProduto("9856TE");
                fail("Erro com a função pesquisaProduto(), pois ela está retornano informações sobre um produto com sku que não foi cadastrada na lista de produtos");
            } catch (GerenciadorProdutosException e) {
                assertEquals("Não foi possivel encontrar o produto de sku 9856TE informado", e.getMessage());
            }

        } catch (ProdutoException e) {
            fail("Erro ao instanciar Produto");
        }

    }

    @Test
    void persistenciaDadosArquivo() {
        assertTrue(gerenciador.listarProdutos().equals("Nenhum produto foi cadastrado."));
        try {
            assertTrue(gerenciador.cadastrarProduto(computador));
            assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());

            try {
                gerenciador.salvarDados();
            } catch (IOException e){
                fail("Erro ao salvar os dados");
            }

            try{
                gerenciador.recuperarDados();
                assertEquals("LISTA DE PRODUTOS CADASTRADOS:\n\n" + "\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", gerenciador.listarProdutos());
            } catch (IOException e) {
                fail("Erro ao recuperar os dados");
            }

        } catch (GerenciadorProdutosException e) {
            fail("Erro, problema com a função cadastrarProduto()");
        }


    }

}
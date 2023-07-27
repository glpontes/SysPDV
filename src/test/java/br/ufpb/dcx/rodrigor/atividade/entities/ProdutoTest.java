package br.ufpb.dcx.rodrigor.atividade.entities;

import br.ufpb.dcx.rodrigor.atividade.exceptions.ProdutoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    Produto produto;

    @BeforeEach
    void setUp() {
        try{
            produto = new Produto("Computador", "1236DE", 3500.00, 10);
        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }

    }

    @Test
    void testCriarProduto(){
        // teste Criando um produto seguindo as regras impostas
        try {
            Produto p = new Produto("Telefone", "3214TL", 1500.00, 4);
        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }

        // teste criando um produto não seguindo a regra imposta para a atribuição do nome
        try {
            Produto p = new Produto(null, "3214TL", 1500.00, 4);
            fail("Erro, foi instanciado um produto com o nome = null");
        } catch (ProdutoException e){
            assertEquals("Erro, as informações passadas sobre o Produto não são válidas\nNome: null\nSKU: 3214TL\nValor: 1500.0\nQuantidade: 4", e.getMessage());
        }

        // teste criando um produto não seguindo a regra imposta para a atribuição do SKU
        try {
            Produto p = new Produto("Telefone", null, 1500.00, 4);
            fail("Erro, foi instanciado um produto com o SKU = null");
        } catch (ProdutoException e){
            assertEquals("Erro, as informações passadas sobre o Produto não são válidas\nNome: Telefone\nSKU: null\nValor: 1500.0\nQuantidade: 4", e.getMessage());
        }

        // teste criando um produto não seguindo a regra imposta para a atribuição do valor
        try {
            Produto p = new Produto("Telefone","3214TL", -1500.00, 4);
            fail("Erro, foi instanciado um produto com o valor negativo");
        } catch (ProdutoException e){
            assertEquals("Erro, as informações passadas sobre o Produto não são válidas\nNome: Telefone\nSKU: 3214TL\nValor: -1500.0\nQuantidade: 4", e.getMessage());
        }

        // teste criando um produto não seguindo a regra imposta para a atribuição da quantidade
        try {
            Produto p = new Produto("Telefone","3214TL", 1500.00, -4);
            fail("Erro, foi instanciado um produto com a quantidade negativo");
        } catch (ProdutoException e){
            assertEquals("Erro, as informações passadas sobre o Produto não são válidas\nNome: Telefone\nSKU: 3214TL\nValor: 1500.0\nQuantidade: -4", e.getMessage());
        }

    }

    @Test
    void getSku() {
        assertEquals("1236DE", produto.getSku());
    }

    @Test
    void getNome() {
        assertEquals("Computador", produto.getNome());
    }

    @Test
    void getValor() {
        assertEquals(3500.00, produto.getValor());
    }

    @Test
    void getQuantidade() {
        assertEquals(10, produto.getQuantidade());
    }

    @Test
    void setSku() {
        assertEquals("1236DE", produto.getSku());
        produto.setSku("3548CS");
        assertEquals("3548CS", produto.getSku());
    }

    @Test
    void setNome() {
        assertEquals("Computador", produto.getNome());
        produto.setNome("Celular");
        assertEquals("Celular", produto.getNome());
    }

    @Test
    void setValor() {
        assertEquals(3500.00, produto.getValor());
        produto.setValor(2000.00);
        assertEquals(2000.00, produto.getValor());
    }

    @Test
    void setQuantidade() {
        assertEquals(10, produto.getQuantidade());
        produto.setQuantidade(15);
        assertEquals(15, produto.getQuantidade());
    }

    @Test
    void testToString() {
        assertEquals("\tProduto: Computador\n\tSku: 1236DE\n\tValor: 3500.0\n\tQuantidade: 10\n\n", produto.toString());
        try {
            Produto celular = new Produto("Celular", "3548CS", 2000.00, 15);
            assertEquals("\tProduto: Celular\n\tSku: 3548CS\n\tValor: 2000.0\n\tQuantidade: 15\n\n", celular.toString());
        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }
    }

    @Test
    void testEquals() {
        try {
            Produto notebook = new Produto("Notebook", "2341NO", 4000.00, 6);
            Produto smartphone = new Produto("Smartphone", "8724SM", 1500.00, 9);
            assertFalse(notebook.equals(smartphone));
            Produto televisao = new Produto("Televisão", "2341NO", 3000.00, 12);
            assertTrue(notebook.getSku().equals(televisao.getSku()));
            assertEquals(notebook, televisao);
            smartphone = notebook;
            assertEquals(notebook, smartphone);
        } catch (ProdutoException e){
            fail("Erro ao instanciar Produto");
        }
    }

}
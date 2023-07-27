package br.ufpb.dcx.rodrigor.atividade.entities;

import br.ufpb.dcx.rodrigor.atividade.exceptions.GerenciadorProdutosException;

import java.io.IOException;


public interface GerenciadorProdutosInterface {

    boolean cadastrarProduto(Produto produLido) throws GerenciadorProdutosException;
    boolean removerProduto(String nomeProduto) throws GerenciadorProdutosException;
    String listarProdutos();
    String pesquisaProduto(String sku) throws GerenciadorProdutosException;
    Produto getProdutoNome(String nomeProduto) throws GerenciadorProdutosException;
    Produto getProdutoSku(String skuProduto) throws GerenciadorProdutosException;
    void salvarDados() throws IOException;
    void recuperarDados() throws IOException;

}

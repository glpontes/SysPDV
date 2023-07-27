package br.ufpb.dcx.rodrigor.atividade.entities;

import br.ufpb.dcx.rodrigor.atividade.exceptions.GerenciadorProdutosException;
import br.ufpb.dcx.rodrigor.atividade.util.Gravador;
import br.ufpb.dcx.rodrigor.atividade.exceptions.ProdutoException;

import java.io.IOException;
import java.util.*;

public class GerenciadorProdutos implements GerenciadorProdutosInterface {

    private Map<String, Produto> produtos;
    private Gravador gravador = new Gravador("Produtos.txt");

    public GerenciadorProdutos(){
        this.produtos = new HashMap<>();
    }

    @Override
    public boolean cadastrarProduto(Produto produtoLido) throws GerenciadorProdutosException {
        if(this.produtos.containsKey(produtoLido.getSku())) {
            if(this.produtos.get(produtoLido.getSku()).getNome().equals(produtoLido.getNome())){
                throw new GerenciadorProdutosException("O produto informado já está cadastrado no sistema");
            }
            throw new GerenciadorProdutosException("A SKU informado já está cadastrado no sistema e relacionada a um produto diferente do informado");
        }
        else{
            this.produtos.put(produtoLido.getSku(), produtoLido);
            return true;
        }
    }

    @Override
    public boolean removerProduto(String nomeProduto) throws GerenciadorProdutosException {
        if(getProdutoNome(nomeProduto) != null) {
            this.produtos.remove(getProdutoNome(nomeProduto).getSku());
            return true;
        }
        else{
            throw new GerenciadorProdutosException("Não foi possivel remover o produto pois, o produto " + nomeProduto + " informado não se encontra na lista de produtos");
        }
    }

    @Override
    public Produto getProdutoNome(String nomeProduto) throws GerenciadorProdutosException {
        if(this.produtos.isEmpty()) {
           throw new GerenciadorProdutosException("Não existe produtos cadastrados");
        }
        else{
            for (String key : this.produtos.keySet()) {
                if (this.produtos.get(key).getNome().equals(nomeProduto)) {
                    return this.produtos.get(key);
                }
            }
            return null;
        }
    }

    @Override
    public Produto getProdutoSku(String skuProduto) throws GerenciadorProdutosException {
        if(this.produtos.isEmpty()){
            throw new GerenciadorProdutosException("Não existe produtos cadastrados");
        }
        else {
            if (this.produtos.containsKey(skuProduto)) {
                return this.produtos.get(skuProduto);
            }
            return null;
        }
    }

    @Override
    public String listarProdutos() {
        String texto = "";
        if (this.produtos.isEmpty()) {
            texto += "Nenhum produto foi cadastrado.";
        } else {
            texto += "LISTA DE PRODUTOS CADASTRADOS:\n\n";
            for (String key : this.produtos.keySet()) {
                Produto produto = this.produtos.get(key);
                texto += produto.toString();
            }
        }
        return texto;
    }


    @Override
    public String pesquisaProduto(String skuProduto) throws GerenciadorProdutosException{
        if(getProdutoSku(skuProduto) != null) {
            return this.produtos.get(skuProduto).toString();
        }
        else{
            throw new GerenciadorProdutosException("Não foi possivel encontrar o produto de sku " + skuProduto + " informado");
        }
    }
    @Override
    public void salvarDados() throws IOException {
        ArrayList<String> texto = new ArrayList<>();
        for (String sku: this.produtos.keySet()){
            Produto produto = this.produtos.get(sku);
            String linha = produto.getNome()+","+produto.getSku()+","+produto.getValor()+","+produto.getQuantidade();
            texto.add(linha);
        }
        gravador.gravaArquivo(texto);
    }

    @Override
    public void recuperarDados() throws IOException {
        try{
            List<String> textoLido = gravador.recuperaArquivo();
            for (String linha: textoLido){
                String [] elementosLinha = linha.split(",");
                Produto produtoLido = new Produto(elementosLinha[0], elementosLinha[1],
                        Double.parseDouble(elementosLinha[2]), Integer.parseInt(elementosLinha[3]));
                this.produtos.put(produtoLido.getSku(), produtoLido);
            }
        }catch(IOException e){
            this.produtos = new HashMap<>();
        } catch (ProdutoException e) {
            e.getMessage();
        }
    }

}

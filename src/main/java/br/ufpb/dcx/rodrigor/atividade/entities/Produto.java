package br.ufpb.dcx.rodrigor.atividade.entities;

import br.ufpb.dcx.rodrigor.atividade.exceptions.ProdutoException;

import java.util.Objects;

public class Produto {
    private String sku;
    private double valor;
    private String nome;
    private int quantidade;

    public Produto(String nome, String sku, double valor, int quantidade) throws ProdutoException {
        if(nome != null && sku != null && valor >= 0.0 && quantidade >= 0) {
            this.nome = nome;
            this.sku = sku;
            this.valor = valor;
            this.quantidade = quantidade;
        }
         else{
            throw new ProdutoException("Erro, as informações passadas sobre o Produto não são válidas\nNome: " + nome + "\nSKU: " + sku + "\nValor: " + valor + "\nQuantidade: " + quantidade);
        }
    }

    public String getSku(){
        return this.sku;
    }
    public String getNome(){
        return this.nome;
    }
    public Double getValor(){
        return this.valor;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setSku(String sku){
        this.sku = sku;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setValor(Double valor){
        this.valor = valor;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }

    @Override
    public String toString(){
        return ("\tProduto: "+nome+"\n\tSku: " + sku+"\n\tValor: "+valor+"\n\tQuantidade: "+quantidade+"\n\n");
    }
    @Override
    public boolean equals(Object obj){
        Produto outro = (Produto) obj;
        return this.sku.equals(outro.getSku());
    }
    @Override
    public int hashCode() {
        return Objects.hash(sku, valor, nome);
    }

}
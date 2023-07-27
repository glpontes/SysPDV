package br.ufpb.dcx.rodrigor.atividade.application;

import br.ufpb.dcx.rodrigor.atividade.entities.Desconto;
import br.ufpb.dcx.rodrigor.atividade.entities.GerenciadorProdutos;
import br.ufpb.dcx.rodrigor.atividade.entities.GerenciadorProdutosInterface;
import br.ufpb.dcx.rodrigor.atividade.entities.Produto;
import br.ufpb.dcx.rodrigor.atividade.exceptions.GerenciadorProdutosException;
import br.ufpb.dcx.rodrigor.atividade.exceptions.ProdutoException;
import br.ufpb.dcx.rodrigor.atividade.util.Texto;

import java.io.IOException;

public class SistemaPDV {

    private GerenciadorProdutosInterface gerenciador;
    private int tamLinha;
    private int margem = 2;
    private String nomeLoja;
    public SistemaPDV(String nomeLoja){
        this.gerenciador = new GerenciadorProdutos();
        this.nomeLoja = nomeLoja;
        this.tamLinha = nomeLoja.length() + 10;
    }

    public static void main(String[] args){
        SistemaPDV pdv = new SistemaPDV("Geraldo Enterprise");
        pdv.run();
    }

    public void run(){
        try {
            gerenciador.recuperarDados();
            printMenuPrincipal();
        } catch (IOException e){
            Texto.printMargem(margem, e.getMessage());
        }
    }

    public void printMenuPrincipal(){
        boolean sair = false;
        while(!sair){
            String[] opcoes = {"1", "2", "3", "4", "5", "x"};
            String[] textoItens = {"Cadastrar produto",
                                   "Listar produtos cadastrados",
                                   "Pesquisar produto no estoque",
                                   "Remover produto do estoque",
                                   "Iniciar Venda",
                                   "Sair"};

            String opcao = printMenu(this.nomeLoja, opcoes, textoItens);
            switch(opcao){
                case "1":
                    cadastrarProduto();
                    break;
                case "2":
                    listarProdutos();
                    break;
                case "3":
                    pesquisarProduto();
                    break;
                case "4":
                    removerProduto();
                    break;
                case "5":
                    iniciarVenda();
                    break;
                case "x":
                    Texto.printMargem(margem,"Até logo!");
                    sair = true;
                    try {
                        gerenciador.salvarDados();
                    } catch (IOException e){
                        Texto.printMargem(margem, e.getMessage());
                    }
                    break;
            }
        }
    }

    public String printMenu(String titulo, String[] opcoes, String[] textoItens){
        if(opcoes.length != textoItens.length) {
            throw new IllegalArgumentException("O número de opções deve ser igual ao número de itens do menu");
        }

        Texto.printCabecalho(titulo, tamLinha);
        for (int i = 0; i < opcoes.length; i++) {
            Texto.printMargem(margem,opcoes[i]+"\t"+textoItens[i]);
        }
        Texto.printLine(tamLinha);
        return Texto.selecionarOpcao(opcoes);
    }

    public void cadastrarProduto(){
        Texto.printLine(tamLinha);
        String nomeProduto = Texto.readString("Nome do Produto:");
        String skuProduto = Texto.readString("Sku do Produto:");
        double valor = Texto.readDouble("Valor do Produto:");
        int quantidade = Texto.readInt("Quantidade do Produto:");
        try {
            Produto produto = new Produto(nomeProduto, skuProduto, valor, quantidade);
            try {
                boolean cadastrou = gerenciador.cadastrarProduto(produto);
                if(cadastrou){
                    Texto.printMargem(margem, "O produto foi cadastrado com sucesso");
                    try {
                        gerenciador.salvarDados();
                    } catch (IOException e){
                        Texto.printMargem(margem, e.getMessage());
                    }

                }
            } catch (GerenciadorProdutosException e){
                Texto.printMargem(margem, e.getMessage());
            }
        } catch (ProdutoException e){
            Texto.printMargem(margem, e.getMessage());
        }
    }

    public void listarProdutos(){
        Texto.printLine(tamLinha);
        Texto.printMargem(margem, gerenciador.listarProdutos());
    }

    public void pesquisarProduto(){
        Texto.printLine(tamLinha);
        String skuProduto = Texto.readString("SKU do Produto: ");
        try {
            Texto.printMargem(margem, "\n" + gerenciador.pesquisaProduto(skuProduto));
        } catch (GerenciadorProdutosException e) {
            Texto.printMargem(margem, e.getMessage());
        }
    }

    public void removerProduto(){
        Texto.printLine(tamLinha);
        String nomeProduto = Texto.readString("Nome do Produto: ");
        try {
            boolean removeu = gerenciador.removerProduto(nomeProduto);
            if(removeu){
                Texto.printMargem(margem, "O produto foi removido com sucesso!!!");
                try {
                    gerenciador.salvarDados();
                } catch (IOException e){
                    Texto.printMargem(margem, e.getMessage());
                }
            }
        } catch (GerenciadorProdutosException e){
            Texto.printMargem(margem, e.getMessage());
        }
    }

    public void iniciarVenda() {
        Desconto desconto = new Desconto();
        Texto.printCabecalho("INÍCIO DO PROCESSO DE VENDA", tamLinha);
        Texto.printMargem(margem,"Para compras acima de R$100 à vista temos desconto de 5%");
        Texto.printMargem(margem, "Para compras acima de R$1.900 à vista temos desconto de 10%\n");
        String skuProduto = Texto.readString("SKU do produto:");
        int quantidadeVenda = Texto.readInt("Quantidade desejada:");
        try {
            double totalVenda = gerenciador.getProdutoSku(skuProduto).getValor() * quantidadeVenda;
                //descontar = true;
                if (gerenciador.getProdutoSku(skuProduto).getQuantidade() >= quantidadeVenda) {
                    String Avista = Texto.readString("A compra é a vista ? s/n: ");
                    if (Avista.equalsIgnoreCase("S") || Avista.equalsIgnoreCase("SIM")) {
                        if (totalVenda > 100 && totalVenda <= 1900) {
                            desconto.descontarCinco(totalVenda);
                            gerenciador.getProdutoSku(skuProduto).setQuantidade(gerenciador.getProdutoSku(skuProduto).getQuantidade() - quantidadeVenda);
                            Texto.printMargem(margem, "\nProduto Escolhido");
                            Texto.printMargem(margem, "Produto: " + gerenciador.getProdutoSku(skuProduto).getNome());
                            Texto.printMargem(margem, "SKU: " + skuProduto);
                            Texto.printMargem(margem, "Quantidade: " + quantidadeVenda);
                            Texto.printMargem(margem, "Preço Total com o desconto de 5%: " + desconto.getValorComDesconto() + "\n");
                            Texto.printCabecalho("FIM DO PROCESSO DE VENDA!!", tamLinha);
                            try {
                                gerenciador.salvarDados();
                            } catch (IOException e){
                                Texto.printMargem(margem, e.getMessage());
                            }
                        } else if (totalVenda > 1900) {
                            desconto.descontarDez(totalVenda);
                            gerenciador.getProdutoSku(skuProduto).setQuantidade(gerenciador.getProdutoSku(skuProduto).getQuantidade() - quantidadeVenda);
                            Texto.printMargem(margem, "\nProduto Escolhido");
                            Texto.printMargem(margem, "Produto: " + gerenciador.getProdutoSku(skuProduto).getNome());
                            Texto.printMargem(margem, "SKU: " + skuProduto);
                            Texto.printMargem(margem, "Quantidade: " + quantidadeVenda);
                            Texto.printMargem(margem, "Preço Total com o desconto de 10%: " + desconto.getValorComDesconto() + "\n");
                            Texto.printCabecalho("FIM DO PROCESSO DE VENDA!!", tamLinha);
                            try {
                                gerenciador.salvarDados();
                            } catch (IOException e){
                                Texto.printMargem(margem, e.getMessage());
                            }
                        } else {
                            gerenciador.getProdutoSku(skuProduto).setQuantidade(gerenciador.getProdutoSku(skuProduto).getQuantidade() - quantidadeVenda);
                            Texto.printMargem(margem, "\nProduto Escolhido");
                            Texto.printMargem(margem, "Produto: " + gerenciador.getProdutoSku(skuProduto).getNome());
                            Texto.printMargem(margem, "SKU: " + skuProduto);
                            Texto.printMargem(margem, "Quantidade: " + quantidadeVenda);
                            Texto.printMargem(margem, "Preço Total: " + totalVenda + "\n");
                            Texto.printCabecalho("FIM DO PROCESSO DE VENDA!!", tamLinha);
                            try {
                                gerenciador.salvarDados();
                            } catch (IOException e){
                                Texto.printMargem(margem, e.getMessage());
                            }
                        }
                    } else {
                        gerenciador.getProdutoSku(skuProduto).setQuantidade(gerenciador.getProdutoSku(skuProduto).getQuantidade() - quantidadeVenda);
                        Texto.printMargem(margem, "\nProduto Escolhido");
                        Texto.printMargem(margem, "Produto: " + gerenciador.getProdutoSku(skuProduto).getNome());
                        Texto.printMargem(margem, "SKU: " + skuProduto);
                        Texto.printMargem(margem, "Quantidade: " + quantidadeVenda);
                        Texto.printMargem(margem, "Preço Total: " + totalVenda + "\n");
                        Texto.printCabecalho("FIM DO PROCESSO DE VENDA!!", tamLinha);
                        try {
                            gerenciador.salvarDados();
                        } catch (IOException e){
                            Texto.printMargem(margem, e.getMessage());
                        }
                    }
                }else{
                Texto.printMargem(margem, "Quantidade disponível no estoque: " + gerenciador.getProdutoSku(skuProduto).getQuantidade() + "    Quantidade desejada:  " + quantidadeVenda);
                Texto.printMargem(margem, "A quantidade do produto no estoque é inferior a quantidade desejada para a venda");
                Texto.printCabecalho("FIM DO PROCESSO DE VENDA!!", tamLinha);
            }

        } catch(GerenciadorProdutosException e){
            Texto.printMargem(margem, e.getMessage());
        }
    }
}

package br.ufpb.dcx.rodrigor.atividade.entities;

public class Desconto {
    private double valorComDesconto;
    public static final double DESC_5 = 0.05;
    public static final double DESC_10 = 0.10;

    public double getValorComDesconto() {
        return valorComDesconto;
    }

    public void descontarCinco(double valor){
        this.valorComDesconto = valor - (valor*DESC_5);
    }

    public void descontarDez(double valor){
        this.valorComDesconto = valor - (valor*DESC_10);
    }


}

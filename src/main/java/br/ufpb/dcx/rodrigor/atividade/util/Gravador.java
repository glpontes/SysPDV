package br.ufpb.dcx.rodrigor.atividade.util;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
    public class Gravador{
        private String nomeArquivo;
        public Gravador(){
            this.nomeArquivo = "Arquivo.txt";
        }

        public Gravador(String nomeArquivo){
            this.nomeArquivo = nomeArquivo;
        }

        public void gravaArquivo(List<String> texto) throws IOException{
            BufferedWriter gravador = null;
            try {
                gravador = new BufferedWriter(new FileWriter(this.nomeArquivo));
                for (String s: texto){
                    gravador.write(s+"\n");
                }
            } finally {
                if (gravador!=null){
                    gravador.close();
                }
            }
        }
        public ArrayList<String> recuperaArquivo() throws IOException{
            BufferedReader leitor = null;
            ArrayList<String> textoLido = new ArrayList<>();
            try {
                leitor = new BufferedReader(new FileReader(this.nomeArquivo));
                String texto = null;
                do {
                    texto = leitor.readLine();
                    if (texto!=null){
                        textoLido.add(texto);
                    }
                } while(texto!=null);
            } finally {
                if (leitor!=null){
                    leitor.close();
                }
            }
            return textoLido;
            
        }



    }

package util;

import exception.ArquivoException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileManager {


    private static final String PASTA_DADOS = "dados/";


    public static void criarPastaDados() {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    
    public static void escrever(String nomeArquivo, List<String> linhas) throws ArquivoException {
        criarPastaDados();
        String caminho = PASTA_DADOS + nomeArquivo;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ArquivoException("gravar", nomeArquivo, e);
        } finally {
            System.out.println("[FileManager] Operação de escrita concluída: " + nomeArquivo);
        }
    }

    
    public static List<String> ler(String nomeArquivo) throws ArquivoException {
        String caminho = PASTA_DADOS + nomeArquivo;
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(caminho);

        
        if (!arquivo.exists()) {
            return linhas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(linha);
                }
            }
        } catch (IOException e) {
            throw new ArquivoException("ler", nomeArquivo, e);
        } finally {
            System.out.println("[FileManager] Operação de leitura concluída: " + nomeArquivo);
        }

        return linhas;
    }
}

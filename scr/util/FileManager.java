package util;

import exception.ArquivoException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe auxiliar responsável pela leitura e escrita de arquivos de texto (.csv).
 * Todos os DAOs utilizam esta classe para persistir e recuperar dados.
 *
 * Cada arquivo armazena uma entidade por linha, com campos separados por ";".
 */
public class FileManager {

    // Pasta onde os arquivos de dados serão salvos
    private static final String PASTA_DADOS = "dados/";

    /**
     * Garante que a pasta "dados/" exista antes de qualquer operação.
     */
    public static void criarPastaDados() {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    /**
     * Grava uma lista de linhas em um arquivo, sobrescrevendo o conteúdo anterior.
     *
     * @param nomeArquivo Nome do arquivo (sem o caminho)
     * @param linhas      Lista de strings a serem gravadas (uma por linha)
     * @throws ArquivoException se ocorrer erro de I/O
     */
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
            // finally garante que o log seja sempre exibido, mesmo em caso de erro
            System.out.println("[FileManager] Operação de escrita concluída: " + nomeArquivo);
        }
    }

    /**
     * Lê todas as linhas de um arquivo e retorna como lista de strings.
     * Retorna lista vazia se o arquivo ainda não existir (primeira execução).
     *
     * @param nomeArquivo Nome do arquivo (sem o caminho)
     * @return Lista de linhas lidas
     * @throws ArquivoException se o arquivo existir mas não puder ser lido
     */
    public static List<String> ler(String nomeArquivo) throws ArquivoException {
        String caminho = PASTA_DADOS + nomeArquivo;
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(caminho);

        // Arquivo ainda não existe: retorna lista vazia (sistema novo)
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

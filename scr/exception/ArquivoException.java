package exception;

/**
 * Lançada quando ocorre um erro ao ler ou gravar arquivos de dados do sistema.
 */
public class ArquivoException extends AlugaAiException {

    public ArquivoException(String operacao, String nomeArquivo, Throwable causa) {
        super("Erro ao " + operacao + " o arquivo \"" + nomeArquivo + "\": " + causa.getMessage(), causa);
    }
}

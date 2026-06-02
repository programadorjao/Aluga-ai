package exception;


public class ArquivoException extends AlugaAiException {

    public ArquivoException(String operacao, String nomeArquivo, Throwable causa) {
        super("Erro ao " + operacao + " o arquivo \"" + nomeArquivo + "\": " + causa.getMessage(), causa);
    }
}

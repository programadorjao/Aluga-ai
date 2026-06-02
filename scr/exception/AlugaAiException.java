package exception;

/**
 * Exceção base do sistema AlugaAí.
 * Todas as exceções customizadas do projeto herdam desta classe.
 */
public class AlugaAiException extends Exception {

    public AlugaAiException(String mensagem) {
        super(mensagem);
    }

    public AlugaAiException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

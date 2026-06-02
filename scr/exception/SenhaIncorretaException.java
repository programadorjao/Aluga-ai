package exception;

/**
 * Lançada quando a senha informada não confere com a senha cadastrada.
 */
public class SenhaIncorretaException extends AlugaAiException {

    public SenhaIncorretaException() {
        super("Senha incorreta. Verifique e tente novamente.");
    }
}

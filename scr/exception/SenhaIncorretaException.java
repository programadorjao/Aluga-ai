package exception;


public class SenhaIncorretaException extends AlugaAiException {

    public SenhaIncorretaException() {
        super("Senha incorreta. Verifique e tente novamente.");
    }
}

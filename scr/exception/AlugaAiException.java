package exception;


public class AlugaAiException extends Exception {

    public AlugaAiException(String mensagem) {
        super(mensagem);
    }

    public AlugaAiException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

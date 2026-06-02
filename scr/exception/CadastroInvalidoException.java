package exception;


public class CadastroInvalidoException extends AlugaAiException {

    public CadastroInvalidoException(String campo) {
        super("Dado inválido no cadastro: " + campo + " não pode ser vazio ou nulo.");
    }
}

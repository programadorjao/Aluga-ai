package exception;

/**
 * Lançada quando dados de cadastro (usuário ou produto) são inválidos ou incompletos.
 */
public class CadastroInvalidoException extends AlugaAiException {

    public CadastroInvalidoException(String campo) {
        super("Dado inválido no cadastro: " + campo + " não pode ser vazio ou nulo.");
    }
}

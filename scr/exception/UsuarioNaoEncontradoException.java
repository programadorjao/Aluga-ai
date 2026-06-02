package exception;

/**
 * Lançada quando um usuário não é encontrado no sistema
 * (ex: e-mail ou ID inexistente).
 */
public class UsuarioNaoEncontradoException extends AlugaAiException {

    public UsuarioNaoEncontradoException(String identificador) {
        super("Usuário não encontrado: " + identificador);
    }
}

package exception;


public class UsuarioNaoEncontradoException extends AlugaAiException {

    public UsuarioNaoEncontradoException(String identificador) {
        super("Usuário não encontrado: " + identificador);
    }
}

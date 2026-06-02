package exception;


public class ProdutoIndisponivelException extends AlugaAiException {

    public ProdutoIndisponivelException(String nomeProduto) {
        super("O produto \"" + nomeProduto + "\" não está disponível para aluguel no momento.");
    }
}

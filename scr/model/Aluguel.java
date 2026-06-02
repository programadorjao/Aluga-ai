package model;

import exception.ProdutoIndisponivelException;

import java.time.Duration;
import java.time.LocalDateTime;

public class Aluguel {
    private int id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String status;

    private Produto produto;
    private Usuario cliente;
    private Usuario dono;

    public Aluguel(Produto produto, Usuario cliente, Usuario dono) {
        this.produto = produto;
        this.cliente = cliente;
        this.dono    = dono;
    }

    /**
     * Solicita o aluguel verificando disponibilidade.
     * @throws ProdutoIndisponivelException se o produto não estiver disponível
     */
    public void solicitarAluguel() throws ProdutoIndisponivelException {
        produto.verificarDisponibilidade(); // lança exceção se indisponível
        this.status = "Pendente";
    }

    public void aprovarAluguel() {
        status = "Aprovado";
        produto.alterarDisponibilidade(false);
    }

    public void finalizarAluguel() {
        status = "Finalizado";
        produto.alterarDisponibilidade(true);
    }

    public void cancelarAluguel() {
        status = "Cancelado";
        produto.alterarDisponibilidade(true);
    }

    public double calcularValor() {
        if (dataInicio == null || dataFim == null) {
            System.out.println("Datas inválidas para cálculo.");
            return 0;
        }
        if (dataFim.isBefore(dataInicio)) {
            System.out.println("Data final não pode ser anterior à data inicial.");
            return 0;
        }
        long horas  = Duration.between(dataInicio, dataFim).toHours();
        double dias = horas / 24.0;
        return dias * produto.getPrecoPorDia();
    }

    // getters
    public Produto getProduto()  { return produto; }
    public Usuario getCliente()  { return cliente; }
    public Usuario getDono()     { return dono; }
    public String getStatus()    { return status; }

    // setter usado pelo DAO para restaurar status do arquivo
    public void setStatus(String status) { this.status = status; }
}

package model;

import exception.ProdutoIndisponivelException;

public class Produto {
    private static int contador = 1;

    private int id;
    private String nome;
    private String descricao;
    private double precoPorDia;
    private boolean disponibilidade;
    private Categoria categoria;

    public Produto(String nome, String descricao, double precoPorDia, Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula.");
        }
        this.id = contador++;
        this.nome = nome;
        this.descricao = descricao;
        this.precoPorDia = precoPorDia;
        this.categoria = categoria;
        this.disponibilidade = true;
    }

    private boolean nomeInvalido()  { return nome == null || nome.isEmpty(); }
    private boolean precoInvalido() { return precoPorDia <= 0; }

    public boolean produtoValido()  { return !nomeInvalido() && !precoInvalido(); }

    public void editarProduto(String novoNome, String novaDescricao, double novoPreco) {
        if (novoNome != null && !novoNome.isEmpty()) this.nome = novoNome;
        if (novoPreco > 0) this.precoPorDia = novoPreco;
        this.descricao = novaDescricao;
    }

    public void alterarDisponibilidade(boolean status) { this.disponibilidade = status; }
    public boolean estaDisponivel()                    { return disponibilidade; }

   
    public void verificarDisponibilidade() throws ProdutoIndisponivelException {
        if (!disponibilidade) {
            throw new ProdutoIndisponivelException(nome);
        }
    }

    // getters
    public int getId()             { return id; }
    public String getNome()        { return nome; }
    public String getDescricao()   { return descricao; }
    public double getPrecoPorDia() { return precoPorDia; }
    public Categoria getCategoria(){ return categoria; }

    // setters usados pelo DAO na recarga
    public void setId(int id) { this.id = id; }

    /** Permite ao DAO restaurar o contador após carga do arquivo */
    public static void setContador(int valor) { contador = valor; }
}

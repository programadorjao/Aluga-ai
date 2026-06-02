package model;

public class Categoria {
    private String nome;

    public Categoria(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Categoria inválida");
            return;
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
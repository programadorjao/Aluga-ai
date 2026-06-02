package model;

import java.util.ArrayList;

public class CategoriaService {
    private ArrayList<Categoria> categorias = new ArrayList<>();

    public void adicionarCategoria(String nome) {
        Categoria categoria = new Categoria(nome);
        categorias.add(categoria);
    }

    public Categoria buscarPorNome(String nome) {
        for (Categoria c : categorias) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria não encontrada: " + nome);
    }
}
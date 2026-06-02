package dao;

import exception.ArquivoException;
import exception.CadastroInvalidoException;
import model.Categoria;
import model.CategoriaService;
import model.Produto;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;


public class ProdutoDAO {

    private static final String ARQUIVO = "produtos.csv";
    private List<Produto> produtos = new ArrayList<>();


    public void adicionar(Produto produto) throws CadastroInvalidoException, ArquivoException {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new CadastroInvalidoException("Nome do produto");
        }
        if (produto.getPrecoPorDia() <= 0) {
            throw new CadastroInvalidoException("Preço por dia (deve ser maior que zero)");
        }

        produtos.add(produto);
        salvar();
    }


    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }


    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        return null;
    }


    public List<Produto> listarDisponiveis() {
        List<Produto> disponiveis = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.estaDisponivel()) disponiveis.add(p);
        }
        return disponiveis;
    }


    public void atualizar(Produto produto) throws ArquivoException {
        salvar();
    }


    public void remover(int id) throws ArquivoException {
        produtos.removeIf(p -> p.getId() == id);
        salvar();
    }


    public void salvar() throws ArquivoException {
        List<String> linhas = new ArrayList<>();
        for (Produto p : produtos) {
            // Formato: id;nome;descricao;precoPorDia;disponibilidade;nomeCategoria
            String linha = p.getId() + ";" + p.getNome() + ";" + p.getDescricao()
                    + ";" + p.getPrecoPorDia() + ";" + p.estaDisponivel()
                    + ";" + p.getCategoria().getNome();
            linhas.add(linha);
        }
        FileManager.escrever(ARQUIVO, linhas);
    }


    public void carregar(CategoriaService categoriaService) throws ArquivoException {
        List<String> linhas = FileManager.ler(ARQUIVO);
        produtos.clear();

        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                if (partes.length < 6) continue;

                int id               = Integer.parseInt(partes[0].trim());
                String nome          = partes[1].trim();
                String descricao     = partes[2].trim();
                double preco         = Double.parseDouble(partes[3].trim());
                boolean disponivel   = Boolean.parseBoolean(partes[4].trim());
                String nomeCategoria = partes[5].trim();

                Categoria cat = categoriaService.buscarPorNome(nomeCategoria);
                Produto p = new Produto(nome, descricao, preco, cat);
                p.setId(id);
                p.alterarDisponibilidade(disponivel);
                produtos.add(p);

            } catch (IllegalArgumentException e) {
                System.out.println("[ProdutoDAO] Linha ignorada: " + linha + " | Motivo: " + e.getMessage());
            }
        }

        // Atualiza o contador estático para evitar conflito de IDs
        int maxId = produtos.stream().mapToInt(Produto::getId).max().orElse(0);
        Produto.setContador(maxId + 1);
    }
}

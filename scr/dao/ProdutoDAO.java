package dao;

import exception.ArquivoException;
import exception.CadastroInvalidoException;
import model.Categoria;
import model.CategoriaService;
import model.Produto;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) da entidade Produto.
 *
 * Responsabilidades:
 * - Manter a lista de produtos em memória.
 * - Persistir e recuperar produtos no arquivo "produtos.csv".
 * - Oferecer operações de CRUD.
 *
 * Formato do arquivo produtos.csv:
 *   id;nome;descricao;precoPorDia;disponibilidade;nomeCategoria
 */
public class ProdutoDAO {

    private static final String ARQUIVO = "produtos.csv";
    private List<Produto> produtos = new ArrayList<>();

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Adiciona um produto validado à lista e persiste.
     *
     * @throws CadastroInvalidoException se nome ou preço forem inválidos
     * @throws ArquivoException          se não for possível salvar
     */
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

    /**
     * Retorna cópia da lista de produtos.
     */
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }

    /**
     * Busca produto pelo ID. Retorna null se não encontrado.
     */
    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Retorna somente os produtos com disponibilidade = true.
     */
    public List<Produto> listarDisponiveis() {
        List<Produto> disponiveis = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.estaDisponivel()) disponiveis.add(p);
        }
        return disponiveis;
    }

    /**
     * Persiste as alterações de um produto já existente na lista.
     *
     * @throws ArquivoException se não for possível salvar
     */
    public void atualizar(Produto produto) throws ArquivoException {
        salvar();
    }

    /**
     * Remove um produto pelo ID e persiste.
     *
     * @throws ArquivoException se não for possível salvar
     */
    public void remover(int id) throws ArquivoException {
        produtos.removeIf(p -> p.getId() == id);
        salvar();
    }

    // -------------------------------------------------------------------------
    // Persistência (arquivo)
    // -------------------------------------------------------------------------

    /**
     * Serializa a lista de produtos e grava no arquivo CSV.
     */
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

    /**
     * Lê o arquivo CSV e recarrega a lista de produtos em memória.
     * Precisa do CategoriaService para reconstruir as referências de Categoria.
     */
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

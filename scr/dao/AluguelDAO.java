package dao;

import exception.ArquivoException;
import model.Aluguel;
import model.Produto;
import model.Usuario;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) da entidade Aluguel.
 *
 * Responsabilidades:
 * - Manter a lista de aluguéis em memória.
 * - Persistir e recuperar aluguéis no arquivo "alugueis.csv".
 * - Oferecer operações de CRUD.
 *
 * Formato do arquivo alugueis.csv:
 *   index;idProduto;idCliente;status
 *
 * Nota: As referências a Produto e Usuario são reconstruídas pelos IDs
 * usando os DAOs correspondentes na carga inicial.
 */
public class AluguelDAO {

    private static final String ARQUIVO = "alugueis.csv";
    private List<Aluguel> alugueis = new ArrayList<>();

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Adiciona um aluguel à lista e persiste.
     */
    public void adicionar(Aluguel aluguel) throws ArquivoException {
        alugueis.add(aluguel);
        salvar();
    }

    /**
     * Retorna cópia da lista de aluguéis.
     */
    public List<Aluguel> listarTodos() {
        return new ArrayList<>(alugueis);
    }

    /**
     * Busca aluguel pelo índice na lista.
     * Retorna null se índice for inválido.
     */
    public Aluguel buscarPorIndice(int index) {
        if (index < 0 || index >= alugueis.size()) return null;
        return alugueis.get(index);
    }

    /**
     * Persiste as alterações de um aluguel (ex: mudança de status).
     */
    public void atualizar() throws ArquivoException {
        salvar();
    }

    // -------------------------------------------------------------------------
    // Persistência (arquivo)
    // -------------------------------------------------------------------------

    /**
     * Serializa a lista de aluguéis e grava no arquivo CSV.
     */
    public void salvar() throws ArquivoException {
        List<String> linhas = new ArrayList<>();
        for (Aluguel a : alugueis) {
            // Formato: idProduto;idCliente;status
            String linha = a.getProduto().getId() + ";" + a.getCliente().getId()
                    + ";" + a.getStatus();
            linhas.add(linha);
        }
        FileManager.escrever(ARQUIVO, linhas);
    }

    /**
     * Lê o arquivo CSV e recarrega a lista de aluguéis.
     * Usa os DAOs de Produto e Usuário para recriar as referências de objetos.
     */
    public void carregar(ProdutoDAO produtoDAO, UsuarioDAO usuarioDAO) throws ArquivoException {
        List<String> linhas = FileManager.ler(ARQUIVO);
        alugueis.clear();

        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                if (partes.length < 3) continue;

                int idProduto  = Integer.parseInt(partes[0].trim());
                int idCliente  = Integer.parseInt(partes[1].trim());
                String status  = partes[2].trim();

                Produto produto = produtoDAO.buscarPorId(idProduto);
                Usuario cliente = usuarioDAO.buscarPorId(idCliente);

                if (produto == null || cliente == null) {
                    System.out.println("[AluguelDAO] Aluguel ignorado (produto ou usuário não encontrado): " + linha);
                    continue;
                }

                Aluguel a = new Aluguel(produto, cliente, null);
                a.setStatus(status); // restaura o status salvo
                alugueis.add(a);

            } catch (Exception e) {
                System.out.println("[AluguelDAO] Linha ignorada: " + linha + " | Motivo: " + e.getMessage());
            }
        }
    }
}

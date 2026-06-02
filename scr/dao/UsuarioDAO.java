package dao;

import exception.ArquivoException;
import exception.CadastroInvalidoException;
import exception.UsuarioNaoEncontradoException;
import model.Usuario;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) da entidade Usuario.
 *
 * Responsabilidades:
 * - Manter a lista de usuários em memória durante a execução.
 * - Persistir (salvar) e recuperar (carregar) usuários no arquivo "usuarios.csv".
 * - Oferecer operações de CRUD: Create, Read, Update, Delete.
 *
 * Formato do arquivo usuarios.csv:
 *   id;nome;email;senha;telefone
 */
public class UsuarioDAO {

    private static final String ARQUIVO = "usuarios.csv";
    private List<Usuario> usuarios = new ArrayList<>();

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Adiciona um novo usuário à lista e persiste no arquivo.
     *
     * @throws CadastroInvalidoException se nome ou e-mail estiverem vazios
     * @throws ArquivoException          se não for possível salvar no arquivo
     */
    public void adicionar(Usuario usuario) throws CadastroInvalidoException, ArquivoException {
        // Validação com exceção customizada
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new CadastroInvalidoException("Nome");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new CadastroInvalidoException("E-mail");
        }

        usuarios.add(usuario);
        salvar(); // persiste a lista inteira após cada alteração
    }

    /**
     * Retorna todos os usuários carregados.
     */
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    /**
     * Busca um usuário pelo e-mail (case-insensitive).
     *
     * @throws UsuarioNaoEncontradoException se nenhum usuário tiver esse e-mail
     */
    public Usuario buscarPorEmail(String email) throws UsuarioNaoEncontradoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        throw new UsuarioNaoEncontradoException(email);
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @throws UsuarioNaoEncontradoException se o ID não existir
     */
    public Usuario buscarPorId(int id) throws UsuarioNaoEncontradoException {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        throw new UsuarioNaoEncontradoException("ID " + id);
    }

    /**
     * Atualiza os dados do usuário na lista e persiste.
     *
     * @throws UsuarioNaoEncontradoException se o ID não existir
     * @throws ArquivoException              se não for possível salvar no arquivo
     */
    public void atualizar(int id, String novoNome, String novoTelefone)
            throws UsuarioNaoEncontradoException, ArquivoException {
        Usuario u = buscarPorId(id);
        u.atualizarDados(novoNome, novoTelefone);
        salvar();
    }

    /**
     * Remove um usuário pelo ID e persiste a lista atualizada.
     *
     * @throws UsuarioNaoEncontradoException se o ID não existir
     * @throws ArquivoException              se não for possível salvar no arquivo
     */
    public void remover(int id) throws UsuarioNaoEncontradoException, ArquivoException {
        Usuario u = buscarPorId(id);
        usuarios.remove(u);
        salvar();
    }

    // -------------------------------------------------------------------------
    // Persistência (arquivo)
    // -------------------------------------------------------------------------

    /**
     * Serializa a lista de usuários e grava no arquivo CSV via FileManager.
     */
    public void salvar() throws ArquivoException {
        List<String> linhas = new ArrayList<>();
        for (Usuario u : usuarios) {
            // Formato: id;nome;email;senha;telefone
            String linha = u.getId() + ";" + u.getNome() + ";" + u.getEmail()
                    + ";" + u.getSenha() + ";" + u.getTelefone();
            linhas.add(linha);
        }
        FileManager.escrever(ARQUIVO, linhas);
    }

    /**
     * Lê o arquivo CSV e recarrega a lista de usuários em memória.
     * Chamado uma vez quando o sistema inicia.
     */
    public void carregar() throws ArquivoException {
        List<String> linhas = FileManager.ler(ARQUIVO);
        usuarios.clear();

        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                // Formato esperado: id;nome;email;senha;telefone
                if (partes.length < 5) continue;

                int id           = Integer.parseInt(partes[0].trim());
                String nome      = partes[1].trim();
                String email     = partes[2].trim();
                String senha     = partes[3].trim();
                String telefone  = partes[4].trim();

                Usuario u = new Usuario(nome, email, senha, telefone);
                u.setId(id); // restaura o ID salvo
                usuarios.add(u);

            } catch (NumberFormatException e) {
                System.out.println("[UsuarioDAO] Linha ignorada (formato inválido): " + linha);
            }
        }
    }
}

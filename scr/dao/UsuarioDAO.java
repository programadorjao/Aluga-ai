package dao;

import exception.ArquivoException;
import exception.CadastroInvalidoException;
import exception.UsuarioNaoEncontradoException;
import model.Usuario;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {

    private static final String ARQUIVO = "usuarios.csv";
    private List<Usuario> usuarios = new ArrayList<>();


    public void adicionar(Usuario usuario) throws CadastroInvalidoException, ArquivoException {
        // Validação com exceção customizada
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new CadastroInvalidoException("Nome");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new CadastroInvalidoException("E-mail");
        }

        usuarios.add(usuario);
        salvar(); 
    }


    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }


    public Usuario buscarPorEmail(String email) throws UsuarioNaoEncontradoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        throw new UsuarioNaoEncontradoException(email);
    }

    
    public Usuario buscarPorId(int id) throws UsuarioNaoEncontradoException {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        throw new UsuarioNaoEncontradoException("ID " + id);
    }


    public void atualizar(int id, String novoNome, String novoTelefone)
            throws UsuarioNaoEncontradoException, ArquivoException {
        Usuario u = buscarPorId(id);
        u.atualizarDados(novoNome, novoTelefone);
        salvar();
    }

 
    public void remover(int id) throws UsuarioNaoEncontradoException, ArquivoException {
        Usuario u = buscarPorId(id);
        usuarios.remove(u);
        salvar();
    }


    public void salvar() throws ArquivoException {
        List<String> linhas = new ArrayList<>();
        for (Usuario u : usuarios) {
           
            String linha = u.getId() + ";" + u.getNome() + ";" + u.getEmail()
                    + ";" + u.getSenha() + ";" + u.getTelefone();
            linhas.add(linha);
        }
        FileManager.escrever(ARQUIVO, linhas);
    }

 
    public void carregar() throws ArquivoException {
        List<String> linhas = FileManager.ler(ARQUIVO);
        usuarios.clear();

        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                
                if (partes.length < 5) continue;

                int id           = Integer.parseInt(partes[0].trim());
                String nome      = partes[1].trim();
                String email     = partes[2].trim();
                String senha     = partes[3].trim();
                String telefone  = partes[4].trim();

                Usuario u = new Usuario(nome, email, senha, telefone);
                u.setId(id); 
                usuarios.add(u);

            } catch (NumberFormatException e) {
                System.out.println("[UsuarioDAO] Linha ignorada (formato inválido): " + linha);
            }
        }
    }
}

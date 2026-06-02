package dao;

import exception.ArquivoException;
import exception.UsuarioNaoEncontradoException;
import model.Aluguel;
import model.Produto;
import model.Usuario;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;


public class AluguelDAO {

    private static final String ARQUIVO = "alugueis.csv";
    private List<Aluguel> alugueis = new ArrayList<>();


    public void adicionar(Aluguel aluguel) throws ArquivoException {
        alugueis.add(aluguel);
        salvar();
    }


    public List<Aluguel> listarTodos() {
        return new ArrayList<>(alugueis);
    }


    public Aluguel buscarPorIndice(int index) {
        if (index < 0 || index >= alugueis.size()) return null;
        return alugueis.get(index);
    }


    public void atualizar() throws ArquivoException {
        salvar();
    }


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
                a.setStatus(status); 
                alugueis.add(a);

            } catch (UsuarioNaoEncontradoException e) {
                System.out.println("[AluguelDAO] Aluguel ignorado (usuário não encontrado): " + linha + " | Motivo: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[AluguelDAO] Linha ignorada: " + linha + " | Motivo: " + e.getMessage());
            }
        }
    }
}

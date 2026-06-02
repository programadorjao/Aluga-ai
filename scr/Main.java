import dao.AluguelDAO;
import dao.ProdutoDAO;
import dao.UsuarioDAO;
import exception.*;
import model.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);


        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ProdutoDAO produtoDAO = new ProdutoDAO();
        AluguelDAO aluguelDAO = new AluguelDAO();

        CategoriaService categoriaService = new CategoriaService();

        
        categoriaService.adicionarCategoria("Automóveis");
        categoriaService.adicionarCategoria("Imóveis");
        categoriaService.adicionarCategoria("Casa e Decoração");
        categoriaService.adicionarCategoria("Móveis");
        categoriaService.adicionarCategoria("Construção");
        categoriaService.adicionarCategoria("Eletro");
        categoriaService.adicionarCategoria("Celulares");
        categoriaService.adicionarCategoria("Informática");
        categoriaService.adicionarCategoria("Games");
        categoriaService.adicionarCategoria("Tvs");
        categoriaService.adicionarCategoria("Agro");
        categoriaService.adicionarCategoria("Serviços");

        
        System.out.println("\n[Sistema] Carregando dados salvos...");
        try {
            usuarioDAO.carregar();
            produtoDAO.carregar(categoriaService);
            aluguelDAO.carregar(produtoDAO, usuarioDAO);
            System.out.println("[Sistema] Dados carregados com sucesso!\n");
        } catch (ArquivoException e) {
            System.out.println("[AVISO] Não foi possível carregar dados anteriores: " + e.getMessage());
        }

        Usuario usuarioLogado = null;
        int opcao = 0;

        do {
            System.out.println("\n==============================");
            System.out.println("         ALUGA AÍ             ");
            System.out.println("==============================");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Login");
            System.out.println("3 - Anunciar Produto");
            System.out.println("4 - Ver Produtos Disponíveis");
            System.out.println("5 - Alugar Produto");
            System.out.println("6 - Gerenciar Meus Aluguéis");
            System.out.println("7 - Editar Meus Dados");
            System.out.println("8 - Categorias");
            System.out.println("9 - Sair");
            System.out.println("==============================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número.");
                continue;
            }

            switch (opcao) {

                case 1:
                    System.out.println("\n--- Cadastro de Usuário ---");
                    System.out.print("Nome: ");
                    String nome = input.nextLine();
                    System.out.print("E-mail: ");
                    String email = input.nextLine();
                    System.out.print("Senha: ");
                    String senha = input.nextLine();
                    System.out.print("Telefone: ");
                    String telefone = input.nextLine();

                    try {
                        Usuario novoUsuario = new Usuario(nome, email, senha, telefone);
                        usuarioDAO.adicionar(novoUsuario); 
                        System.out.println("Usuário \"" + nome + "\" cadastrado com sucesso!");
                    } catch (CadastroInvalidoException e) {
                        System.out.println("Erro de cadastro: " + e.getMessage());
                    } catch (ArquivoException e) {
                        System.out.println("Erro ao salvar usuário: " + e.getMessage());
                    } finally {
                        System.out.println("[Log] Tentativa de cadastro de usuário concluída.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- Login ---");
                    System.out.print("E-mail: ");
                    String emailLogin = input.nextLine();
                    System.out.print("Senha: ");
                    String senhaLogin = input.nextLine();

                    try {
                        Usuario candidato = usuarioDAO.buscarPorEmail(emailLogin); 
                        if (!candidato.login(emailLogin, senhaLogin)) {
                            throw new SenhaIncorretaException();
                        }
                        usuarioLogado = candidato;
                        System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
                    } catch (UsuarioNaoEncontradoException e) {
                        System.out.println("Erro de login: " + e.getMessage());
                    } catch (SenhaIncorretaException e) {
                        System.out.println("Erro de login: " + e.getMessage());
                    }
                    break;


                case 3:
                    if (usuarioLogado == null) {
                        System.out.println("Você precisa fazer login primeiro.");
                        break;
                    }

                    System.out.println("\n--- Anunciar Produto ---");
                    System.out.print("Nome do produto: ");
                    String nomeProduto = input.nextLine();
                    System.out.print("Descrição: ");
                    String descricao = input.nextLine();

                    double preco = 0;
                    try {
                        System.out.print("Preço por dia (R$): ");
                        preco = Double.parseDouble(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Preço inválido. Use números (ex: 25.50).");
                        break;
                    }

                    System.out.print("Categoria: ");
                    String nomeCategoria = input.nextLine();

                    try {
                        Categoria cat = categoriaService.buscarPorNome(nomeCategoria);
                        Produto novoProduto = new Produto(nomeProduto, descricao, preco, cat);
                        produtoDAO.adicionar(novoProduto); // CadastroInvalidoException | ArquivoException
                        System.out.println("Produto \"" + nomeProduto + "\" anunciado! ID: " + novoProduto.getId());
                    } catch (CadastroInvalidoException e) {
                        System.out.println("Erro de cadastro: " + e.getMessage());
                    } catch (ArquivoException e) {
                        System.out.println("Erro ao salvar produto: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    } finally {
                        System.out.println("[Log] Tentativa de anúncio de produto concluída.");
                    }
                    break;

                case 4:
                    System.out.println("\n--- Produtos Disponíveis ---");
                    List<Produto> disponiveis = produtoDAO.listarDisponiveis();
                    if (disponiveis.isEmpty()) {
                        System.out.println("Nenhum produto disponível no momento.");
                    } else {
                        for (Produto p : disponiveis) {
                            System.out.println("---------------------------");
                            System.out.println("ID:        " + p.getId());
                            System.out.println("Nome:      " + p.getNome());
                            System.out.println("Descrição: " + p.getDescricao());
                            System.out.printf("Preço/dia: R$ %.2f%n", p.getPrecoPorDia());
                            System.out.println("Categoria: " + p.getCategoria().getNome());
                        }
                    }
                    break;

                case 5:
                    if (usuarioLogado == null) {
                        System.out.println("Você precisa fazer login primeiro.");
                        break;
                    }

                    System.out.println("\n--- Alugar Produto ---");
                    System.out.print("Digite o ID do produto: ");
                    int idProduto;
                    try {
                        idProduto = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                        break;
                    }

                    Produto produtoEscolhido = produtoDAO.buscarPorId(idProduto);
                    if (produtoEscolhido == null) {
                        System.out.println("Produto não encontrado.");
                        break;
                    }

                    try {
                        Aluguel novoAluguel = new Aluguel(produtoEscolhido, usuarioLogado, null);
                        novoAluguel.solicitarAluguel(); // lança ProdutoIndisponivelException
                        aluguelDAO.adicionar(novoAluguel);
                        System.out.println("Solicitação de aluguel enviada! Aguardando aprovação.");
                    } catch (ProdutoIndisponivelException e) {
                        System.out.println("Erro: " + e.getMessage());
                    } catch (ArquivoException e) {
                        System.out.println("Erro ao salvar aluguel: " + e.getMessage());
                    }
                    break;

                case 6:
                    if (usuarioLogado == null) {
                        System.out.println("Você precisa fazer login primeiro.");
                        break;
                    }

                    System.out.println("\n--- Gerenciar Aluguéis ---");
                    System.out.println("1 - Aprovar aluguel");
                    System.out.println("2 - Cancelar aluguel");
                    System.out.println("3 - Finalizar aluguel");
                    System.out.print("Opção: ");
                    int subOpcaoAluguel;
                    try {
                        subOpcaoAluguel = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida.");
                        break;
                    }

                    System.out.print("Digite o índice do aluguel (começa em 0): ");
                    int idxAluguel;
                    try {
                        idxAluguel = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Índice inválido.");
                        break;
                    }

                    Aluguel aluguelSelecionado = aluguelDAO.buscarPorIndice(idxAluguel);
                    if (aluguelSelecionado == null) {
                        System.out.println("Aluguel não encontrado.");
                        break;
                    }

                    try {
                        switch (subOpcaoAluguel) {
                            case 1:
                                aluguelSelecionado.aprovarAluguel();
                                aluguelDAO.atualizar();
                                System.out.println("Aluguel aprovado com sucesso!");
                                break;
                            case 2:
                                aluguelSelecionado.cancelarAluguel();
                                aluguelDAO.atualizar();
                                produtoDAO.atualizar(aluguelSelecionado.getProduto());
                                System.out.println("Aluguel cancelado.");
                                break;
                            case 3:
                                aluguelSelecionado.finalizarAluguel();
                                aluguelDAO.atualizar();
                                produtoDAO.atualizar(aluguelSelecionado.getProduto());
                                System.out.println("Aluguel finalizado. Produto disponível novamente.");
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    } catch (ArquivoException e) {
                        System.out.println("Erro ao atualizar dados: " + e.getMessage());
                    }
                    break;

                case 7:
                    if (usuarioLogado == null) {
                        System.out.println("Você precisa fazer login primeiro.");
                        break;
                    }

                    System.out.println("\n--- Editar Meus Dados ---");
                    System.out.println("1 - Atualizar nome e telefone");
                    System.out.println("2 - Alterar senha");
                    System.out.print("Opção: ");
                    int subOpcaoUsuario;
                    try {
                        subOpcaoUsuario = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida.");
                        break;
                    }

                    if (subOpcaoUsuario == 1) {
                        System.out.print("Novo nome: ");
                        String novoNome = input.nextLine();
                        System.out.print("Novo telefone: ");
                        String novoTel = input.nextLine();
                        try {
                            usuarioDAO.atualizar(usuarioLogado.getId(), novoNome, novoTel);
                            System.out.println("Dados atualizados com sucesso!");
                        } catch (UsuarioNaoEncontradoException | ArquivoException e) {
                            System.out.println("Erro ao atualizar: " + e.getMessage());
                        }

                    } else if (subOpcaoUsuario == 2) {
                        System.out.print("Senha atual: ");
                        String senhaAtual = input.nextLine();
                        System.out.print("Nova senha: ");
                        String novaSenha = input.nextLine();
                        try {
                            usuarioLogado.alterarSenha(senhaAtual, novaSenha); // SenhaIncorretaException
                            usuarioDAO.salvar();
                            System.out.println("Senha alterada com sucesso!");
                        } catch (SenhaIncorretaException e) {
                            System.out.println("Erro: " + e.getMessage());
                        } catch (ArquivoException e) {
                            System.out.println("Erro ao salvar nova senha: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;

                case 8:
                    System.out.println("\n--- Categorias ---");
                    System.out.println("1 - Adicionar categoria");
                    System.out.println("2 - Buscar categoria");
                    System.out.print("Opção: ");
                    int subOpcaoCategoria;
                    try {
                        subOpcaoCategoria = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida.");
                        break;
                    }

                    if (subOpcaoCategoria == 1) {
                        System.out.print("Nome da nova categoria: ");
                        String novaCategoria = input.nextLine();
                        categoriaService.adicionarCategoria(novaCategoria);
                        System.out.println("Categoria \"" + novaCategoria + "\" adicionada!");
                    } else if (subOpcaoCategoria == 2) {
                        System.out.print("Nome da categoria: ");
                        String busca = input.nextLine();
                        try {
                            Categoria resultado = categoriaService.buscarPorNome(busca);
                            System.out.println("Categoria encontrada: " + resultado.getNome());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;

                case 9:
                    System.out.println("\nSaindo do Aluga Aí... Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 9);

        input.close();
    }
}

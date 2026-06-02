package model;

import exception.SenhaIncorretaException;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private static int contador = 1;

    public Usuario(String nome, String email, String senha, String telefone) {
        this.id = contador++;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public void verificarCadastro() {
        if (nome == null || email == null) {
            System.out.println("Dados inválidos");
            return;
        }
        System.out.println("Usuário cadastrado com sucesso");
    }

    public boolean login(String emailDigitado, String senhaDigitada) {
        return this.email.equals(emailDigitado) && this.senha.equals(senhaDigitada);
    }

    public void atualizarDados(String novoNome, String novoTelefone) {
        this.nome = novoNome;
        this.telefone = novoTelefone;
    }


    public void alterarSenha(String atual, String nova) throws SenhaIncorretaException {
        if (!this.senha.equals(atual)) {
            throw new SenhaIncorretaException();
        }
        this.senha = nova;
    }

    // getters
    public int getId()         { return id; }
    public String getNome()    { return nome; }
    public String getEmail()   { return email; }
    public String getSenha()   { return senha; }
    public String getTelefone(){ return telefone; }

    // setters
    public void setId(int id)              { this.id = id; }
    public void setNome(String nome)       { this.nome = nome; }
    public void setTelefone(String tel)    { this.telefone = tel; }

    /** Permite ao DAO restaurar o contador após carga do arquivo */
    public static void setContador(int valor) {
        contador = valor;
    }
}

package br.com.faculdade.modelo;

public class Professor extends Usuario {

    private String titulacao;

    public Professor() {
        super(); 
        this.setPerfil(PerfilUsuario.PROFESSOR);
    }

    public String getTitulacao() {
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }
}
package br.com.faculdade.modelo;

public class Curso {
    private int id;
    private String nome;
    private int codigoCurso;
    private String descricao;
    private Professor coordenador; 
    private boolean ativo = true;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Integer getCodigoCurso() {
        return codigoCurso;
    }
    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }
    public Professor getCoordenador() {
        return coordenador;
    }
    public void setCoordenador(Professor coordenador) {
        this.coordenador = coordenador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
    return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
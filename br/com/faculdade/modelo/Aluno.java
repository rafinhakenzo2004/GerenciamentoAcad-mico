package br.com.faculdade.modelo;

public class Aluno extends Usuario { 

    private String matricula;

    public Aluno() {
        super();
        this.setPerfil(PerfilUsuario.ALUNO); 
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

private double nota;

public double getNota() {
    return nota;
}

public void setNota(double nota) {
    this.nota = nota;
}
}
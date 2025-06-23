package br.com.faculdade.modelo;

public class Turma {
    private int id;
    private String semestre; 
    private String horario;
    private Disciplina disciplina;
    private Professor professor;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSemestre() {
        return semestre;
    }
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
    public String getHorario() {
        return horario;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }
    public Disciplina getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    public Professor getProfessor() {
        return professor;
    }
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}

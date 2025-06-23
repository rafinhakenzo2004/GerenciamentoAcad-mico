package br.com.faculdade.modelo;

public class Matricula {
    private int id;
    private Aluno aluno;
    private Turma turma;
    private double mediaFinal;
    private double frequenciaPercentual;
    private String status; 

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Aluno getAluno() {
        return aluno;
    }
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    public Turma getTurma() {
        return turma;
    }
    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    public double getMediaFinal() {
        return mediaFinal;
    }
    public void setMediaFinal(double mediaFinal) {
        this.mediaFinal = mediaFinal;
    }
    public double getFrequenciaPercentual() {
        return frequenciaPercentual;
    }
    public void setFrequenciaPercentual(double frequenciaPercentual) {
        this.frequenciaPercentual = frequenciaPercentual;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
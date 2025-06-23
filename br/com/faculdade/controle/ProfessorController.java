package br.com.faculdade.controle;

import br.com.faculdade.dao.MatriculaDAO;
import br.com.faculdade.dao.TurmaDAO;
import br.com.faculdade.modelo.Turma;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProfessorController {

    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();

    public List<Turma> getMinhasTurmas(int idProfessor) {
        try {
            return turmaDAO.findAllByProfessorId(idProfessor);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<br.com.faculdade.modelo.Aluno> getAlunosDaTurma(int idTurma) {
        try {
            return matriculaDAO.findAlunosByTurma(idTurma);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean lancarNotas(Map<Integer, Double> notas, int idTurma) {
        try {
            for (Map.Entry<Integer, Double> entry : notas.entrySet()) {
                int idAluno = entry.getKey();
                double nota = entry.getValue();

                if (nota < 0.0 || nota > 10.0) {
                    throw new IllegalArgumentException("Nota fora do intervalo permitido para o aluno ID: " + idAluno);
                }

                matriculaDAO.updateNota(idAluno, idTurma, nota);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

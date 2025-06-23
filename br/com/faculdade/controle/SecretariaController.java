package br.com.faculdade.controle;

import br.com.faculdade.dao.AlunoDAO;
import br.com.faculdade.dao.MatriculaDAO;
import br.com.faculdade.dao.TurmaDAO;
import br.com.faculdade.modelo.Aluno;
import br.com.faculdade.modelo.Matricula;
import br.com.faculdade.modelo.Turma;
import java.sql.SQLException;
import java.util.List;

public class SecretariaController {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();

    public Aluno buscarAlunoPorId(int idAluno) throws SQLException {
        return alunoDAO.findById(idAluno);
    }

    public Turma buscarTurmaPorId(int idTurma) throws SQLException {
        return turmaDAO.findById(idTurma);
    }

    public boolean realizarMatricula(Aluno aluno, Turma turma) {
        if (aluno == null || turma == null) return false;

        try {
            int vagas = turmaDAO.getVagasDisponiveis(turma.getId());
            if (vagas <= 0) { return false; /* ou lançar exceção */ }
            
            Matricula existente = matriculaDAO.findByAlunoAndTurma(aluno.getId(), turma.getId());
            if (existente != null) { return false; }
            
            Matricula novaMatricula = new Matricula();
            novaMatricula.setAluno(aluno);
            novaMatricula.setTurma(turma);
            novaMatricula.setStatus("CURSANDO");
            
            matriculaDAO.save(novaMatricula);
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

public List<Aluno> getAlunosDaTurma(int idTurma) {
    try {
        return matriculaDAO.findAlunosByTurma(idTurma);
    } catch (SQLException e) {
        e.printStackTrace();
        return List.of(); 
    }
}
}
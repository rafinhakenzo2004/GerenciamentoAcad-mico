package br.com.faculdade.controle;

import br.com.faculdade.dao.CursoDAO;
import br.com.faculdade.dao.ProfessorDAO;
import br.com.faculdade.modelo.Curso;
import br.com.faculdade.modelo.Professor;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CursoController {

    private final CursoDAO cursoDAO = new CursoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    public List<Curso> listarTodosCursos() {
        try {
            return cursoDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void salvarOuAtualizarCurso(Curso curso) {
        try {
            if (curso.getId() > 0) {
                cursoDAO.update(curso);
            } else {
                cursoDAO.save(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desativarCurso(Curso curso) {
        try {
            curso.setAtivo(false); 
            cursoDAO.update(curso);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Professor> listarProfessores() {
        try {
            return professorDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

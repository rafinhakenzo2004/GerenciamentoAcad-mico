package br.com.faculdade.controle;

import br.com.faculdade.dao.CursoDAO;
import br.com.faculdade.dao.RelatorioDAO;
import br.com.faculdade.modelo.Curso;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RelatorioController {

    private final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private final CursoDAO cursoDAO = new CursoDAO();

    public List<Curso> listarCursos() {
        try {
            return cursoDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Map<String, Map<String, Integer>> aprovadosReprovadosPorTurma(int idCurso, String semestre) {
        try {
            return relatorioDAO.buscarAprovadosReprovadosPorTurma(idCurso, semestre);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public double mediaGeralPorCurso(int idCurso, String semestre) {
        try {
            return relatorioDAO.calcularMediaGeralPorCurso(idCurso, semestre);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
} 
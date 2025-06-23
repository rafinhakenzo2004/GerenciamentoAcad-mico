package br.com.faculdade.controle;

import br.com.faculdade.dao.AlunoDAO;
import br.com.faculdade.dao.RelatorioDAO;
import java.util.List;
import java.util.Map;

public class AlunoController {
    private AlunoDAO alunoDAO = new AlunoDAO();
    private RelatorioDAO relatorioDAO = new RelatorioDAO();

    public List<Map<String, Object>> getMeuHistorico(int idAluno) {
        return alunoDAO.getHistoricoCompleto(idAluno);
    }

    public List<Map<String, String>> getMeuHorario(int idAluno, String semestre) {
        return alunoDAO.getHorarioAluno(idAluno, semestre);
    }

    public double getMinhaMediaGeral(int idAluno) {
        return relatorioDAO.calcularMediaHistorico(idAluno);
    }
}
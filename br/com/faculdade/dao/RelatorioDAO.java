package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RelatorioDAO {

    public Map<String, Integer> contarAlunosAprovadosReprovadosPorTurma(int idTurma) {
        String sql = "SELECT " +
                     "  COUNT(CASE WHEN status = 'APROVADO' THEN 1 END) AS total_aprovados, " +
                     "  COUNT(CASE WHEN status LIKE 'REPROVADO%' THEN 1 END) AS total_reprovados " +
                     "FROM TB_MATRICULA WHERE fk_turma = ?";
        
        Map<String, Integer> resultado = new HashMap<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idTurma);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    resultado.put("aprovados", rs.getInt("total_aprovados"));
                    resultado.put("reprovados", rs.getInt("total_reprovados"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public double calcularMediaHistorico(int idAluno) {
        String sql = "SELECT AVG(media_final) AS media_geral FROM TB_MATRICULA " +
                     "WHERE fk_aluno = ? AND status IN ('APROVADO', 'REPROVADO_POR_NOTA')";
        
        double media = 0.0;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idAluno);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    media = rs.getDouble("media_geral");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return media;
    }
    

     public boolean aplicarBonusTurma(int idTurma, double valorBonus) {
        String sql = "UPDATE TB_MATRICULA SET media_final = media_final + ? " +
                     "WHERE fk_turma = ? AND status = 'CURSANDO'";

        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDouble(1, valorBonus);
            pst.setInt(2, idTurma);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public int calcularCargaHorariaTotalDoCurso(int idCurso) {
        String sql = "SELECT SUM(d.carga_horaria) AS carga_total FROM TB_DISCIPLINA d " +
                     "INNER JOIN TB_GRADE_CURRICULAR gc ON d.id_disciplina = gc.fk_disciplina " +
                     "WHERE gc.fk_curso = ?";
        
        int cargaTotal = 0;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idCurso);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cargaTotal = rs.getInt("carga_total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cargaTotal;
    }

    public double obterNotaMaisAltaNaDisciplina(int idDisciplina) {
        String sql = "SELECT MAX(m.media_final) AS nota_maxima FROM TB_MATRICULA m " +
                     "INNER JOIN TB_TURMA t ON m.fk_turma = t.id_turma " +
                     "WHERE t.fk_disciplina = ?";

        double notaMaxima = 0.0;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idDisciplina);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    notaMaxima = rs.getDouble("nota_maxima");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notaMaxima;
    }

    public double calcularMediaGeralPorCurso(int idCurso, String semestre) throws SQLException {
        String sql = 
            "SELECT AVG(m.media_final) AS media_geral " +
            "FROM TB_MATRICULA m " +
            "JOIN TB_TURMA t ON m.fk_turma = t.id_turma " +
            "WHERE t.semestre = ? " +
            "  AND t.fk_disciplina IN (SELECT fk_disciplina FROM TB_GRADE_CURRICULAR WHERE fk_curso = ?) " +
            "  AND m.status IN ('APROVADO', 'REPROVADO_POR_NOTA')";

        double media = 0.0;

        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, semestre);
            pst.setInt(2, idCurso);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    media = rs.getDouble("media_geral");
                }
            }
        }

        return media;
    }

    public Map<String, Map<String, Integer>> buscarAprovadosReprovadosPorTurma(int idCurso, String semestre) throws SQLException {
        String sql = 
            "SELECT t.id_turma, t.semestre, t.horario, " +
            "       m.status, COUNT(*) AS quantidade " +
            "FROM TB_TURMA t " +
            "JOIN TB_MATRICULA m ON t.id_turma = m.fk_turma " +
            "WHERE t.fk_professor IN (SELECT id_professor FROM TB_PROFESSOR) " +
            "  AND t.semestre = ? " +
            "  AND t.fk_disciplina IN (SELECT fk_disciplina FROM TB_GRADE_CURRICULAR WHERE fk_curso = ?) " +
            "GROUP BY t.id_turma, t.semestre, t.horario, m.status";

        Map<String, Map<String, Integer>> resultado = new HashMap<>();

        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, semestre);
            pst.setInt(2, idCurso);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int idTurma = rs.getInt("id_turma");
                    String semestreTurma = rs.getString("semestre");
                    String horario = rs.getString("horario");
                    String status = rs.getString("status");
                    int quantidade = rs.getInt("quantidade");

                    String nomeTurma = "Turma " + idTurma + " (" + semestreTurma + " - " + horario + ")";

                    Map<String, Integer> mapaStatus = resultado.getOrDefault(nomeTurma, new HashMap<>());
                    mapaStatus.put(status, quantidade);
                    resultado.put(nomeTurma, mapaStatus);
                }
            }
        }

        return resultado;
    }
}

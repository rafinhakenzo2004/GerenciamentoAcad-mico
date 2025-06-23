package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import br.com.faculdade.modelo.Disciplina;
import br.com.faculdade.modelo.Professor;
import br.com.faculdade.modelo.Turma;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO implements GenericDAO<Turma> {

    @Override
    public void save(Turma turma) throws SQLException {
        String sql = "INSERT INTO TB_TURMA (semestre, horario, fk_professor, fk_disciplina) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, turma.getSemestre());
            pst.setString(2, turma.getHorario());
            pst.setInt(3, turma.getProfessor().getId());
            pst.setInt(4, turma.getDisciplina().getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Turma turma) throws SQLException {
        String sql = "UPDATE TB_TURMA SET semestre = ?, horario = ?, fk_professor = ?, fk_disciplina = ? WHERE id_turma = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, turma.getSemestre());
            pst.setString(2, turma.getHorario());
            pst.setInt(3, turma.getProfessor().getId());
            pst.setInt(4, turma.getDisciplina().getId());
            pst.setInt(5, turma.getId());
            pst.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TB_TURMA WHERE id_turma = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public Turma findById(int id) throws SQLException {
        String sql = "SELECT t.*, p.nome_completo as professor_nome, d.nome as disciplina_nome " +
                     "FROM TB_TURMA t " +
                     "LEFT JOIN TB_PROFESSOR p ON t.fk_professor = p.id_professor " +
                     "LEFT JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina " +
                     "WHERE t.id_turma = ?";
        Turma turma = null;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    turma = extractTurmaFromResultSet(rs);
                }
            }
        }
        return turma;
    }

    @Override
    public List<Turma> findAll() throws SQLException {
        String sql = "SELECT t.*, p.nome_completo as professor_nome, d.nome as disciplina_nome " +
                     "FROM TB_TURMA t " +
                     "LEFT JOIN TB_PROFESSOR p ON t.fk_professor = p.id_professor " +
                     "LEFT JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina";
        List<Turma> turmas = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    turmas.add(extractTurmaFromResultSet(rs));
                }
            }
        return turmas;
    }
    
    private Turma extractTurmaFromResultSet(ResultSet rs) throws SQLException {
        Turma turma = new Turma();
        turma.setId(rs.getInt("id_turma"));
        turma.setSemestre(rs.getString("semestre"));
        turma.setHorario(rs.getString("horario"));

        Professor professor = new Professor();
        professor.setId(rs.getInt("fk_professor"));
        professor.setNomeCompleto(rs.getString("professor_nome"));
        turma.setProfessor(professor);

        Disciplina disciplina = new Disciplina();
        disciplina.setId(rs.getInt("fk_disciplina"));
        disciplina.setNome(rs.getString("disciplina_nome"));
        turma.setDisciplina(disciplina);
        
        return turma;
    }

public List<Turma> findAllByProfessorId(int idProfessor) throws SQLException {
    String sql = "SELECT t.*, p.nome_completo AS professor_nome, d.nome AS disciplina_nome " +
                 "FROM TB_TURMA t " +
                 "LEFT JOIN TB_PROFESSOR p ON t.fk_professor = p.id_professor " +
                 "LEFT JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina " +
                 "WHERE t.fk_professor = ?";

    List<Turma> turmas = new ArrayList<>();

    try (Connection conn = ConexaoSQLServer.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idProfessor);

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                turmas.add(extractTurmaFromResultSet(rs));
            }
        }
    }

    return turmas;
}

public int getVagasDisponiveis(int idTurma) throws SQLException {
    String sql = """
        SELECT t.capacidade - COUNT(m.fk_aluno) AS vagas
        FROM TB_TURMA t
        LEFT JOIN TB_MATRICULA m ON t.id_turma = m.fk_turma
        WHERE t.id_turma = ?
        GROUP BY t.capacidade
    """;

    try (Connection conn = ConexaoSQLServer.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, idTurma);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("vagas");
        }
    }

    return 0;
}

}
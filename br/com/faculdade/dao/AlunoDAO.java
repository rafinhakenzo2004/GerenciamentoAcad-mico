package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import br.com.faculdade.modelo.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlunoDAO implements GenericDAO<Aluno> {

    @Override
    public void save(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO TB_ALUNO (nome_completo, cpf, data_nascimento, email, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, 'ALUNO')";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, aluno.getNomeCompleto());
            pst.setString(2, aluno.getCpf());
            pst.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            pst.setString(4, aluno.getEmail());
            pst.setString(5, aluno.getLogin());
            pst.setString(6, aluno.getSenha());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Aluno aluno) throws SQLException {
        String sql = "UPDATE TB_ALUNO SET nome_completo = ?, cpf = ?, data_nascimento = ?, email = ? WHERE id_aluno = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, aluno.getNomeCompleto());
            pst.setString(2, aluno.getCpf());
            pst.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            pst.setString(4, aluno.getEmail());
            pst.setInt(5, aluno.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TB_ALUNO WHERE id_aluno = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public Aluno findById(int id) throws SQLException {
        String sql = "SELECT * FROM TB_ALUNO WHERE id_aluno = ?";
        Aluno aluno = null;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    aluno = extractAlunoFromResultSet(rs);
                }
            }
        }
        return aluno;
    }

    @Override
    public List<Aluno> findAll() throws SQLException {
        String sql = "SELECT * FROM TB_ALUNO";
        List<Aluno> alunos = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                alunos.add(extractAlunoFromResultSet(rs));
            }
        }
        return alunos;
    }

    private Aluno extractAlunoFromResultSet(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();
        aluno.setId(rs.getInt("id_aluno"));
        aluno.setNomeCompleto(rs.getString("nome_completo"));
        aluno.setCpf(rs.getString("cpf"));
        aluno.setEmail(rs.getString("email"));
        if (rs.getDate("data_nascimento") != null) {
            aluno.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        }
        return aluno;
    }

public List<Map<String, Object>> getHistoricoCompleto(int idAluno) {
    String sql = """
        SELECT d.nome AS disciplina, m.nota, m.status, t.semestre
        FROM TB_MATRICULA m
        JOIN TB_TURMA t ON m.fk_turma = t.id_turma
        JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina
        WHERE m.fk_aluno = ?
        ORDER BY t.semestre
    """;

    List<Map<String, Object>> historico = new ArrayList<>();
    try (Connection conn = ConexaoSQLServer.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idAluno);
        try (ResultSet rs = pst.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                historico.add(row);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return historico;
}

public List<Map<String, String>> getHorarioAluno(int idAluno, String semestre) {
    String sql = """
        SELECT d.nome AS disciplina, t.horario
        FROM TB_MATRICULA m
        JOIN TB_TURMA t ON m.fk_turma = t.id_turma
        JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina
        WHERE m.fk_aluno = ? AND t.semestre = ?
    """;

    List<Map<String, String>> horario = new ArrayList<>();
    try (Connection conn = ConexaoSQLServer.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idAluno);
        pst.setString(2, semestre);
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("disciplina", rs.getString("disciplina"));
                row.put("horario", rs.getString("horario"));
                horario.add(row);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return horario;
}

}
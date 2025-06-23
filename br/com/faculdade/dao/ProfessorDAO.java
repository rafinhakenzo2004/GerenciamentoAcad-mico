package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import br.com.faculdade.modelo.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO implements GenericDAO<Professor> {

    @Override
    public void save(Professor professor) throws SQLException {
        String sql = "INSERT INTO TB_PROFESSOR (nome_completo, cpf, email, titulacao, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, 'PROFESSOR')";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, professor.getNomeCompleto());
            pst.setString(2, professor.getCpf());
            pst.setString(3, professor.getEmail());
            pst.setString(4, professor.getTitulacao());
            pst.setString(5, professor.getLogin());
            pst.setString(6, professor.getSenha());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Professor professor) throws SQLException {
        String sql = "UPDATE TB_PROFESSOR SET nome_completo = ?, email = ?, titulacao = ? WHERE id_professor = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, professor.getNomeCompleto());
            pst.setString(2, professor.getCpf());
            pst.setString(3, professor.getEmail());
            pst.setString(4, professor.getTitulacao());
            pst.setString(5, professor.getLogin());
            pst.setString(6, professor.getSenha());
            pst.executeUpdate();
        }
    }

        @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TB_PROFESSOR WHERE id_professor = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

        @Override
    public Professor findById(int id) throws SQLException {
        String sql = "SELECT * FROM TB_PROFESSOR WHERE id_professor = ?";
        Professor professor = null;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    professor = extractProfessorFromResultSet(rs);
                }
            }
        }
        return professor;
    }

        @Override
    public List<Professor> findAll() throws SQLException {
        String sql = "SELECT * FROM TB_PROFESSOR";
        List<Professor> professor = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                professor.add(extractProfessorFromResultSet(rs));
            }
        }
        return professor;
    }

        private Professor extractProfessorFromResultSet(ResultSet rs) throws SQLException {
        Professor professor = new Professor();
        professor.setId(rs.getInt("id_professor"));
        professor.setNomeCompleto(rs.getString("nome_completo"));
        professor.setCpf(rs.getString("cpf"));
        professor.setEmail(rs.getString("email"));
        professor.setTitulacao(rs.getString("titulacao"));    

        return professor;
    }
}

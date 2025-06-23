package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import br.com.faculdade.modelo.Disciplina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO implements GenericDAO<Disciplina> {

    @Override
    public void save(Disciplina disciplina) throws SQLException {
        String sql = "INSERT INTO TB_DISCIPLINA (nome, codigo, ementa, carga_horaria) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, disciplina.getNome());
            pst.setString(2, disciplina.getCodigo());
            pst.setString(3, disciplina.getEmenta());
            pst.setInt(4, disciplina.getCargaHoraria());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Disciplina disciplina) throws SQLException {
        String sql = "UPDATE TB_DISCIPLINA SET nome = ?, codigo = ?, ementa = ?, carga_horaria = ? WHERE id_disciplina = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, disciplina.getNome());
            pst.setString(2, disciplina.getCodigo());
            pst.setString(3, disciplina.getEmenta());
            pst.setInt(4, disciplina.getCargaHoraria());
            pst.setInt(5, disciplina.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TB_DISCIPLINA WHERE id_disciplina = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public Disciplina findById(int id) throws SQLException {
        String sql = "SELECT * FROM TB_DISCIPLINA WHERE id_disciplina = ?";
        Disciplina disciplina = null;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    disciplina = extractDisciplinaFromResultSet(rs);
                }
            }
        }
        return disciplina;
    }

    @Override
    public List<Disciplina> findAll() throws SQLException {
        String sql = "SELECT * FROM TB_DISCIPLINA";
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                disciplinas.add(extractDisciplinaFromResultSet(rs));
            }
        }
        return disciplinas;
    }

    private Disciplina extractDisciplinaFromResultSet(ResultSet rs) throws SQLException {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(rs.getInt("id_disciplina"));
        disciplina.setNome(rs.getString("nome"));
        disciplina.setCodigo(rs.getString("codigo"));
        disciplina.setEmenta(rs.getString("ementa"));
        disciplina.setCargaHoraria(rs.getInt("carga_horaria"));
        return disciplina;
    }
}
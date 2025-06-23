package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import br.com.faculdade.modelo.Curso;
import br.com.faculdade.modelo.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO implements GenericDAO<Curso> {

    @Override
    public void save(Curso curso) throws SQLException {
        String sql = "INSERT INTO TB_CURSO (nome, codigo_curso, descricao, fk_coordenador) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, curso.getNome());
            pst.setInt(2, curso.getCodigoCurso());
            pst.setString(3, curso.getDescricao());
            pst.setInt(4, curso.getCoordenador().getId()); 
            pst.executeUpdate();
        }
    }
    
    @Override
    public Curso findById(int codigoCurso) throws SQLException {
        String sql = "SELECT c.*, p.nome_completo as nome_coordenador " +
                     "FROM TB_CURSO c " +
                     "LEFT JOIN TB_PROFESSOR p ON c.fk_coordenador = p.id_professor " +
                     "WHERE c.codigo_curso = ?";
        Curso curso = null;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, codigoCurso);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    curso = new Curso();
                    curso.setId(rs.getInt("id"));
                    curso.setCodigoCurso(rs.getInt("codigo_curso"));
                    curso.setNome(rs.getString("nome"));
                    curso.setDescricao(rs.getString("descricao"));
                    Professor coordenador = new Professor();
                    coordenador.setId(rs.getInt("fk_coordenador"));
                    coordenador.setNomeCompleto(rs.getString("nome_coordenador"));
                    curso.setCoordenador(coordenador);
                }
            }
        }
        return curso;
    }

    @Override
    public void delete(int codigoCurso) throws SQLException {
        String sql = "DELETE FROM TB_CURSO WHERE codigo_curso = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, codigoCurso);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Curso> findAll() throws SQLException {
        String sql = "SELECT * FROM TB_ALUNO";
        List<Curso> curso = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                curso.add(extractCursoFromResultSet(rs));
            }
        }
        return curso;
    }

    private Curso extractCursoFromResultSet(ResultSet rs) throws SQLException {
        Curso curso = new Curso();
        curso.setCodigoCurso(rs.getInt("codigo_Curso"));
        curso.setNome(rs.getString("nome"));
        curso.setDescricao(rs.getString("descricao"));
        Professor coordenador = new Professor();
        coordenador.setId(rs.getInt("fk_coordenador"));
        curso.setCoordenador(coordenador);
        return curso;
    }

    @Override
    public void update(Curso curso) throws SQLException {
        String sql = "UPDATE TB_CURSO SET nome = ?, descricao = ?, fk_coordenador = ? WHERE codigo_curso = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, curso.getDescricao());
            pst.setString(2, curso.getNome());
            pst.setInt(4, curso.getCodigoCurso());
            pst.setInt(5, curso.getCoordenador().getId());
            pst.executeUpdate();
        }
    }

}
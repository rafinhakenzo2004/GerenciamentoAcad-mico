package br.com.faculdade.dao;

import br.com.faculdade.dao.conexao.ConexaoSQLServer;
import br.com.faculdade.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO implements GenericDAO<Matricula> {

    @Override
    public void save(Matricula matricula) throws SQLException {
        String sql = "INSERT INTO TB_MATRICULA (fk_aluno, fk_turma, media_final, frequencia_percentual, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, matricula.getAluno().getId());
            pst.setInt(2, matricula.getTurma().getId());
            pst.setDouble(3, matricula.getMediaFinal());
            pst.setDouble(4, matricula.getFrequenciaPercentual());
            pst.setString(5, matricula.getStatus());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Matricula matricula) throws SQLException {
        // Geralmente, só se atualiza notas, frequência e status de uma matrícula
        String sql = "UPDATE TB_MATRICULA SET media_final = ?, frequencia_percentual = ?, status = ? " +
                     "WHERE id_matricula = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDouble(1, matricula.getMediaFinal());
            pst.setDouble(2, matricula.getFrequenciaPercentual());
            pst.setString(3, matricula.getStatus());
            pst.setInt(4, matricula.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TB_MATRICULA WHERE id_matricula = ?";
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public Matricula findById(int id) throws SQLException {
        String sql = "SELECT m.*, a.nome_completo AS aluno_nome, d.nome AS disciplina_nome, t.semestre " +
                     "FROM TB_MATRICULA m " +
                     "LEFT JOIN TB_ALUNO a ON m.fk_aluno = a.id_aluno " +
                     "LEFT JOIN TB_TURMA t ON m.fk_turma = t.id_turma " +
                     "LEFT JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina " +
                     "WHERE m.id_matricula = ?";
        Matricula matricula = null;
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    matricula = extractMatriculaFromResultSet(rs);
                }
            }
        }
        return matricula;
    }
    
    @Override
    public List<Matricula> findAll() throws SQLException {
        String sql = "SELECT m.*, a.nome_completo AS aluno_nome, d.nome AS disciplina_nome, t.semestre " +
                     "FROM TB_MATRICULA m " +
                     "LEFT JOIN TB_ALUNO a ON m.fk_aluno = a.id_aluno " +
                     "LEFT JOIN TB_TURMA t ON m.fk_turma = t.id_turma " +
                     "LEFT JOIN TB_DISCIPLINA d ON t.fk_disciplina = d.id_disciplina " +
                     "WHERE m.id_matricula = ?";
        List<Matricula> matriculas = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                matriculas.add(extractMatriculaFromResultSet(rs));
            }
        }
        return matriculas;
    }
    
    private Matricula extractMatriculaFromResultSet(ResultSet rs) throws SQLException {
        Matricula matricula = new Matricula();
        matricula.setId(rs.getInt("id_matricula"));
        matricula.setMediaFinal(rs.getDouble("media_final"));
        matricula.setFrequenciaPercentual(rs.getDouble("frequencia_percentual"));
        matricula.setStatus(rs.getString("status"));

        Aluno aluno = new Aluno();
        aluno.setId(rs.getInt("fk_aluno"));
        aluno.setNomeCompleto(rs.getString("aluno_nome"));
        matricula.setAluno(aluno);

        Turma turma = new Turma();
        turma.setId(rs.getInt("fk_turma"));
        turma.setSemestre(rs.getString("semestre"));
        
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(rs.getString("disciplina_nome"));
        turma.setDisciplina(disciplina);
        
        matricula.setTurma(turma);
        
        return matricula;
    }

    public List<Aluno> findAlunosByTurma(int idTurma) throws SQLException {
        String sql = "SELECT a.id_aluno, a.nome_completo, a.cpf, a.email, m.nota " +
                     "FROM TB_MATRICULA m " +
                     "JOIN TB_ALUNO a ON m.fk_aluno = a.id_aluno " +
                     "WHERE m.fk_turma = ?";
        
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idTurma);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = new Aluno();
                    aluno.setId(rs.getInt("id_aluno"));
                    aluno.setNomeCompleto(rs.getString("nome_completo"));
                    aluno.setCpf(rs.getString("cpf"));
                    aluno.setEmail(rs.getString("email"));
                    aluno.setNota(rs.getDouble("nota")); 
                    alunos.add(aluno);
                }
            }
        }

        return alunos;
    }

    public void updateNota(int idAluno, int idTurma, double nota) throws SQLException {
        String sql = "UPDATE TB_MATRICULA SET nota = ? WHERE fk_aluno = ? AND fk_turma = ?";

        try (Connection conn = ConexaoSQLServer.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDouble(1, nota);
            pst.setInt(2, idAluno);
            pst.setInt(3, idTurma);
            pst.executeUpdate();
        }
    }

public Matricula findByAlunoAndTurma(int idAluno, int idTurma) throws SQLException {
    String sql = "SELECT * FROM TB_MATRICULA WHERE fk_aluno = ? AND fk_turma = ?";

    try (Connection conn = ConexaoSQLServer.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idAluno);
        pst.setInt(2, idTurma);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("id_matricula"));
                return matricula;
            }
        }
    }

    return null;
}

}
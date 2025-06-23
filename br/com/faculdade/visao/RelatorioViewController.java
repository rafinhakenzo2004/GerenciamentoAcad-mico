package br.com.faculdade.visao;

import br.com.faculdade.controle.RelatorioController;
import br.com.faculdade.modelo.Curso;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Map;

public class RelatorioViewController {

    @FXML private ComboBox<String> comboTipoRelatorio;
    @FXML private ComboBox<Curso> comboCurso;
    @FXML private TextField txtSemestre;
    @FXML private Button btnGerar;
    @FXML private TextArea txtResultado;

    private RelatorioViewController relatorioController;

    @FXML
    public void initialize() {
        relatorioController = new RelatorioViewController();

        comboTipoRelatorio.setItems(FXCollections.observableArrayList(
                "Aprovados/Reprovados por Turma",
                "Média Geral por Curso"
        ));

        List<Curso> cursos = relatorioController.listarCursos();
        comboCurso.setItems(FXCollections.observableArrayList(cursos));

        btnGerar.setOnAction(e -> gerarRelatorio());
    }

    private void gerarRelatorio() {
        String tipo = comboTipoRelatorio.getValue();
        Curso curso = comboCurso.getValue();
        String semestre = txtSemestre.getText();

        if (tipo == null || curso == null || semestre.isEmpty()) {
            mostrarAlerta("Preencha todos os campos antes de gerar o relatório.");
            return;
        }

        StringBuilder resultado = new StringBuilder();

        if (tipo.equals("Aprovados/Reprovados por Turma")) {
            Map<String, Map<String, Integer>> dados = relatorioController.aprovadosReprovadosPorTurma(curso.getId(), semestre);
            for (String turma : dados.keySet()) {
                resultado.append("Turma: ").append(turma).append("\n");
                Map<String, Integer> status = dados.get(turma);
                resultado.append("  Aprovados: ").append(status.getOrDefault("APROVADO", 0)).append("\n");
                resultado.append("  Reprovados: ").append(status.getOrDefault("REPROVADO", 0)).append("\n\n");
            }

        } else if (tipo.equals("Média Geral por Curso")) {
            double media = relatorioController.mediaGeralPorCurso(curso.getId(), semestre);
            resultado.append("Média geral do curso ").append(curso.getNome()).append(" no semestre ").append(semestre).append(": ").append(String.format("%.2f", media));
        }

        txtResultado.setText(resultado.toString());
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
} 
package br.com.faculdade.visao;

import br.com.faculdade.controle.SecretariaController;
import br.com.faculdade.modelo.Aluno;
import br.com.faculdade.modelo.Turma;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RealizarMatriculaController {

    @FXML private TextField txtIdAluno;
    @FXML private TextField txtIdTurma;
    @FXML private Label lblNomeAluno;
    @FXML private Label lblNomeDisciplina;
    @FXML private Button btnConfirmarMatricula;

    private SecretariaController secretariaController = new SecretariaController();
    private Aluno alunoSelecionado;
    private Turma turmaSelecionada;

    @FXML
    public void initialize() {
        btnConfirmarMatricula.setDisable(true); 
    }
    @FXML
    void handleBuscarAluno(ActionEvent event) {
        try {
            int idAluno = Integer.parseInt(txtIdAluno.getText());
            if (alunoSelecionado != null) {
                lblNomeAluno.setText(alunoSelecionado.getNomeCompleto());
                validarCondicoes();
            } else {
                mostrarAlertaErro("Aluno não encontrado.");
                lblNomeAluno.setText("...");
            }
        } catch (NumberFormatException e) {
            mostrarAlertaErro("ID do Aluno inválido.");
        }
    }

    @FXML
    void handleBuscarTurma(ActionEvent event) {
        try {
            int idTurma = Integer.parseInt(txtIdTurma.getText());
            if (turmaSelecionada != null) {
                lblNomeDisciplina.setText(turmaSelecionada.getDisciplina().getNome());
                validarCondicoes();
            } else {
                mostrarAlertaErro("Turma não encontrada.");
                lblNomeDisciplina.setText("...");
            }
        } catch (NumberFormatException e) {
            mostrarAlertaErro("ID da Turma inválido.");
        }
    }

    private void validarCondicoes() {
        if (alunoSelecionado != null && turmaSelecionada != null) {
            btnConfirmarMatricula.setDisable(false);
        }
    }

    @FXML
    void handleConfirmarMatricula(ActionEvent event) {
        boolean sucesso = true; 
        if (sucesso) {
            mostrarAlertaInfo("Matrícula realizada com sucesso!");
            limparCampos();
        } else {
            mostrarAlertaErro("Não foi possível realizar a matrícula. Verifique as regras de negócio (vagas, pré-requisitos, etc).");
        }
    }

    private void limparCampos() {
        txtIdAluno.clear();
        txtIdTurma.clear();
        lblNomeAluno.setText("...");
        lblNomeDisciplina.setText("...");
        alunoSelecionado = null;
        turmaSelecionada = null;
        btnConfirmarMatricula.setDisable(true);
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
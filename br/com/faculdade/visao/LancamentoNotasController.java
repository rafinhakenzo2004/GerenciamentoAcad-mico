package br.com.faculdade.visao;

import br.com.faculdade.controle.ProfessorController;
import br.com.faculdade.modelo.Aluno;
import br.com.faculdade.modelo.Turma;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LancamentoNotasController {

    private ProfessorController professorController = new ProfessorController();
    private int idProfessorLogado = 1;

    @FXML private ComboBox<Turma> comboTurmas;
    @FXML private TableView<Aluno> tabelaAlunos;
    @FXML private TableColumn<Aluno, String> colMatricula;
    @FXML private TableColumn<Aluno, String> colNomeAluno;
    @FXML private TableColumn<Aluno, Double> colNota;
    @FXML private Button btnSalvar;

    @FXML
    public void initialize() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNomeAluno.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        List<Turma> turmas = professorController.getMinhasTurmas(idProfessorLogado);
        comboTurmas.setItems(FXCollections.observableArrayList(turmas));
    }

    @FXML
    void handleTurmaSelecionada(ActionEvent event) {
        Turma turmaSelecionada = comboTurmas.getValue();
        if (turmaSelecionada != null) {
            List<Aluno> alunos = professorController.getAlunosDaTurma(turmaSelecionada.getId());
            tabelaAlunos.setItems(FXCollections.observableArrayList(alunos));
        }
    }

    @FXML
    void handleSalvarNotas(ActionEvent event) {
        Turma turmaSelecionada = comboTurmas.getValue();
        if (turmaSelecionada == null) {
            return;
        }

        Map<Integer, Double> mapaDeAlunosENotas = new HashMap<>();
        for (Aluno aluno : tabelaAlunos.getItems()) {
            mapaDeAlunosENotas.put(aluno.getId(), aluno.getNota());
        }

    }
}

package br.com.faculdade.visao;

import br.com.faculdade.controle.ProfessorController;
import br.com.faculdade.controle.TurmaController;
import br.com.faculdade.modelo.Professor;
import br.com.faculdade.modelo.Turma;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.stream.Collectors;

public class AtribuirProfessorTurmaController {

    @FXML private TextField txtCodigoTurma;
    @FXML private Label lblDisciplina;
    @FXML private Label lblSemestre;
    @FXML private Label lblProfessorAtual;

    @FXML private TextField txtBuscaProfessor;
    @FXML private TableView<Professor> tabelaProfessores;
    @FXML private TableColumn<Professor, String> colNomeProfessor;
    @FXML private TableColumn<Professor, String> colTitulacao;
    @FXML private Button btnAtribuirProfessor;

    private TurmaController turmaController = new TurmaController();
    private ProfessorController professorController = new ProfessorController();

    private Turma turmaSelecionada;
    private ObservableList<Professor> listaProfessores = FXCollections.observableArrayList();
    private ObservableList<Professor> listaFiltrada = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNomeProfessor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomeCompleto()));
        colTitulacao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulacao()));

        List<Professor> professores = professorController.listarTodosProfessores();
        listaProfessores.setAll(professores);
        listaFiltrada.setAll(professores);
        tabelaProfessores.setItems(listaFiltrada);

        tabelaProfessores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            atualizarEstadoBotao();
        });
    }

@FXML
void handleBuscarTurma() {
    String texto = txtCodigoTurma.getText().trim();
    if (texto.isEmpty()) {
        mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Digite o ID da turma para buscar.");
        return;
    }
    
    int id;
    try {
        id = Integer.parseInt(texto);
    } catch (NumberFormatException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID da turma inválido. Use apenas números.");
        return;
    }
    
    turmaSelecionada = turmaController.buscarTurmaPorCodigo(id);
    if (turmaSelecionada == null) {
        mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Turma não encontrada.");
        limparDetalhesTurma();
        atualizarEstadoBotao();
        return;
    }
    mostrarDetalhesTurma(turmaSelecionada);
    atualizarEstadoBotao();
}


    @FXML
    void handleFiltrarProfessores() {
        String filtro = txtBuscaProfessor.getText().toLowerCase();
        if (filtro.isEmpty()) {
            listaFiltrada.setAll(listaProfessores);
        } else {
            List<Professor> filtrados = listaProfessores.stream()
                .filter(p -> p.getNomeCompleto().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
            listaFiltrada.setAll(filtrados);
        }
    }

    @FXML
    void handleAtribuirProfessor() {
        Professor professorSelecionado = tabelaProfessores.getSelectionModel().getSelectedItem();
        if (turmaSelecionada == null || professorSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione uma turma e um professor.");
            return;
        }
        boolean sucesso = turmaController.atribuirProfessor(turmaSelecionada.getId(), professorSelecionado.getId());
        if (sucesso) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Professor atribuído com sucesso!");
            turmaSelecionada.setProfessor(professorSelecionado);
            mostrarDetalhesTurma(turmaSelecionada);
            atualizarEstadoBotao();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao atribuir o professor.");
        }
    }

    private void mostrarDetalhesTurma(Turma turma) {
        lblDisciplina.setText(turma.getDisciplina().getNome());
        lblSemestre.setText(turma.getSemestre());
        lblProfessorAtual.setText(turma.getProfessor() != null ? turma.getProfessor().getNomeCompleto() : "Nenhum");
    }

    private void limparDetalhesTurma() {
        lblDisciplina.setText("...");
        lblSemestre.setText("...");
        lblProfessorAtual.setText("...");
    }

    private void atualizarEstadoBotao() {
        boolean habilitar = turmaSelecionada != null && tabelaProfessores.getSelectionModel().getSelectedItem() != null;
        btnAtribuirProfessor.setDisable(!habilitar);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

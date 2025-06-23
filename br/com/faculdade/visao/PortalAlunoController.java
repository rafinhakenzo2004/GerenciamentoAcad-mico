package br.com.faculdade.visao;

import br.com.faculdade.controle.AlunoController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import java.util.List;
import java.util.Map;

public class PortalAlunoController {

    private int idAluno;
    private AlunoController alunoController = new AlunoController();

    @FXML private Label lblNomeAluno;
    @FXML private Label lblMediaGeral;
    @FXML private TableView<Map<String, Object>> tabelaHistorico;
    @FXML private TableColumn<Map, String> colDisciplina;
    @FXML private TableColumn<Map, String> colSemestre;
    @FXML private TableColumn<Map, Double> colMediaFinal;
    @FXML private TableColumn<Map, String> colStatus;

    public void initData(int idAluno) {
        this.idAluno = idAluno;
        carregarDados();
    }

    @FXML
    public void initialize() {
        colDisciplina.setCellValueFactory(new MapValueFactory<>("disciplina"));
        colSemestre.setCellValueFactory(new MapValueFactory<>("semestre"));
        colMediaFinal.setCellValueFactory(new MapValueFactory<>("media"));
        colStatus.setCellValueFactory(new MapValueFactory<>("status"));
    }

    private void carregarDados() {
        List<Map<String, Object>> historico = alunoController.getMeuHistorico(this.idAluno);
        if (historico != null) {
            ObservableList<Map<String, Object>> dadosTabela = FXCollections.observableArrayList(historico);
            tabelaHistorico.setItems(dadosTabela);
        }

        double media = alunoController.getMinhaMediaGeral(this.idAluno);
        lblMediaGeral.setText(String.format("Média Geral do Histórico: %.2f", media));
    }
}
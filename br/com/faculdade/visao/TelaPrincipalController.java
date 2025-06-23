package br.com.faculdade.visao;

import br.com.faculdade.controle.SecretariaController;
import br.com.faculdade.modelo.Aluno;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TelaPrincipalController {

    @FXML private TextField txtIdTurma;
    @FXML private TableView<Aluno> tabelaAlunos;
    @FXML private TableColumn<Aluno, Integer> colunaId;
    @FXML private TableColumn<Aluno, String> colunaNome;

    private SecretariaController secretariaController;

    public void initialize() {
        this.secretariaController = new SecretariaController();
        
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
    }

    @FXML
    void handleBuscarAlunos(ActionEvent event) {
        try {
            int idTurma = Integer.parseInt(txtIdTurma.getText());
            
            List<Aluno> alunos = secretariaController.getAlunosDaTurma(idTurma);
            
            tabelaAlunos.setItems(FXCollections.observableArrayList(alunos));
            
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Entrada");
            alert.setHeaderText("ID da Turma inválido.");
            alert.setContentText("Por favor, insira um número válido.");
            alert.showAndWait();
        }
    }
}
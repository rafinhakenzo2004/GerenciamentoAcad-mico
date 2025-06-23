package br.com.faculdade.visao;

import br.com.faculdade.dao.ProfessorDAO;
import br.com.faculdade.modelo.Professor;
import br.com.faculdade.modelo.Turma;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class CadastroTurmaController {

    @FXML
    private ComboBox<Professor> comboBoxProfessor; 

    private ProfessorDAO professorDAO = new ProfessorDAO();

    @FXML
    public void initialize() {
    }

    @FXML
    void handleSalvarTurma(ActionEvent event) {
        Professor professorSelecionado = comboBoxProfessor.getValue();

        Turma novaTurma = new Turma();
        novaTurma.setProfessor(professorSelecionado);
        
    }
}
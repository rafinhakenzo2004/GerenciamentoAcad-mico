package br.com.faculdade.visao;

import br.com.faculdade.controle.CursoController;
import br.com.faculdade.modelo.Curso;
import br.com.faculdade.modelo.Professor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class FormCursoController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private ComboBox<Professor> comboCoordenador;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private CursoController cursoController = new CursoController();
    private Curso cursoAtual;

    @FXML
    public void initialize() {
        List<Professor> professores = cursoController.listarProfessores();
        comboCoordenador.setItems(FXCollections.observableArrayList(professores));
    }

    public void setCurso(Curso curso) {
        this.cursoAtual = curso;
        if (curso != null) {
            txtCodigo.setText(String.valueOf(curso.getId()));  
            txtNome.setText(curso.getNome());
            txtDescricao.setText(curso.getDescricao());
            comboCoordenador.setValue(curso.getCoordenador());
        }
    }

    @FXML
    void salvar() {
        if (cursoAtual == null) {
            cursoAtual = new Curso();
        }

        cursoAtual.setCodigoCurso(Integer.parseInt(txtCodigo.getText()));
        cursoAtual.setNome(txtNome.getText());
        cursoAtual.setDescricao(txtDescricao.getText());
        cursoAtual.setCoordenador(comboCoordenador.getValue());

        cursoController.salvarOuAtualizarCurso(cursoAtual);
        fechar();
    }

    @FXML
    void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
} 

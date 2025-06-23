package br.com.faculdade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import br.com.faculdade.visao.PortalAlunoController;

public class Main extends Application {

    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gerenciamento Acadêmico");

        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout, 1024, 768);

        VBox menuLateral = new VBox();
        menuLateral.setPadding(new Insets(10));
        menuLateral.setSpacing(10);
        menuLateral.setStyle("-fx-background-color: #e0e0e0;");

        Button btnPortalAluno = new Button("Portal do Aluno");
        Button btnLancamentoNotas = new Button("Lançar Notas");
        Button btnRealizarMatricula = new Button("Realizar Matrícula");
        
        btnPortalAluno.setMaxWidth(Double.MAX_VALUE);
        btnLancamentoNotas.setMaxWidth(Double.MAX_VALUE);
        btnRealizarMatricula.setMaxWidth(Double.MAX_VALUE);

        btnPortalAluno.setOnAction(e -> carregarTela("PortalAluno"));
        btnLancamentoNotas.setOnAction(e -> carregarTela("LancamentoNotas"));

        menuLateral.getChildren().addAll(btnPortalAluno, btnLancamentoNotas, btnRealizarMatricula);
        rootLayout.setLeft(menuLateral);

        carregarTela("PortalAluno");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void carregarTela(String nomeTela) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/br/com/faculdade/visao/" + nomeTela + ".fxml"));
            BorderPane tela = loader.load();
            rootLayout.setCenter(tela);

            if ("PortalAluno".equals(nomeTela)) {
                PortalAlunoController controller = loader.getController();
                controller.initData(1001); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
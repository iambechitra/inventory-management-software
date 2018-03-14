package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable{

    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML Button loginButton;
    @FXML Button exitButton;
    @FXML Label label;

    @FXML
    public void onClickLoginButton(ActionEvent event) throws IOException{
        if(username.getText().equals("admin") && passwordField.getText().equals("admin")) {
            Stage stage0 = (Stage) username.getScene().getWindow();
            stage0.close();

            MainWindow("admin_screen.fxml");
        } else {
            label.setText("Username or Password not match!");
        }
    }

    @FXML
    public void onClickExitButton(ActionEvent event) throws IOException{
        Stage stages = (Stage) exitButton.getScene().getWindow();
        stages.close();

        MainWindow("window_main.fxml");
    }

    public void MainWindow(String fxmlResource) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlResource));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Second Screen");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL uri, ResourceBundle rb) {

    }
}

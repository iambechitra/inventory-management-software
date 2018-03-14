package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    Button basicButton;

    @FXML
    public void onClickBasicButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("selling_window.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Second Screen");
        stage.setScene(scene);
        stage.show();

        Stage stage1 = (Stage) basicButton.getScene().getWindow();
        stage1.close();
    }

    public void onClickExitButton (ActionEvent event) throws IOException {
        Platform.exit();
    }

    @FXML
    public void onClickBuilderButton(ActionEvent event) throws IOException {

    }

    @FXML
    public void onClickAdminPanelButtion(ActionEvent event) throws IOException {
        Stage stage1 = (Stage) basicButton.getScene().getWindow();
        stage1.close();

        Parent root = FXMLLoader.load(getClass().getResource("login_screen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}

package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerScreenController implements Initializable {

    @FXML
    TableView<CustomerInformation> tableView;

    @FXML
    Button searchButton;

    @FXML
    TextField searchTextField;

    @FXML
    TableColumn<CustomerInformation, String> distributor, type, size, brand, model, serial, quantity, date, pdate, price, name, phone;

    private DatabaseHelper db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
           db = new DatabaseHelper();
        } catch (Exception e) {

        }

    }

    private void setTableView(ObservableList<CustomerInformation> information) {
        distributor.setCellValueFactory(new PropertyValueFactory<>("distro"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        serial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        date.setCellValueFactory(new PropertyValueFactory<>("sysDate"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        pdate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.setItems(information);
    }

    private ResultSet getResultSet(String keyword) throws SQLException{
        return db.getResults("SELECT * FROM CUSTOMER WHERE NAME ='"+keyword+"' OR PHONE ='"+keyword+"' OR SERIAL ='"+keyword+"'");
    }

    private ObservableList<CustomerInformation> getListOfInformation(ResultSet resultSet) throws SQLException{
        ObservableList<CustomerInformation> information = FXCollections.observableArrayList();

        while (resultSet.next()) {
            information.add(new CustomerInformation(resultSet.getString("NAME"), resultSet.getString("PHONE"),
                    resultSet.getString("ADDRESS"), resultSet.getString("DISTRIBUTOR"), resultSet.getString("TYPE"),
                    resultSet.getString("SIZE"), resultSet.getString("BRAND"), resultSet.getString("MODEL"),
                    resultSet.getString("SERIAL"), resultSet.getString("QUANTITY"), resultSet.getString("PRICE"),
                    resultSet.getString("RPRICE"), resultSet.getString("DATE"), resultSet.getString("SYSDATE")));
        }

        return information;
    }

    @FXML
    private void onClickSearchButton(ActionEvent event) throws SQLException, IOException{
        setTableView(getListOfInformation(getResultSet(searchTextField.getText().toUpperCase())));
    }

    @FXML
    private void onClickCloseButton(ActionEvent event) throws SQLException, IOException {
        db.close();
        Stage stage1 = (Stage)searchButton.getScene().getWindow();
        stage1.close();

        Parent root = FXMLLoader.load(getClass().getResource("admin_screen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}

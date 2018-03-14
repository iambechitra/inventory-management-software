package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


public class SellingWindowController implements Initializable{

    @FXML
    Button backButton;

    @FXML Label cartLabel;

    @FXML
    TableView<OnSaleProduct> tableView;

    @FXML
    ComboBox<String> brandCombobox, productCombobox;

    private DatabaseHelper db;

    @FXML
    TextField modelTextField, searchTextField;

    @FXML
    TableColumn<OnSaleProduct, String> idOfProduct , typeOfProduct, sizeOfProduct, brandOfProduct, modelOfProduct, serialOfProduct, quantityOfProduct, priceOfProduct, realPriceOfProduct;

    private ObservableList<OnSaleProduct> list = FXCollections.observableArrayList();

    @FXML
    public void onClickBackToMain(ActionEvent event) throws IOException, SQLException {
        db.close();
        Stage stages = (Stage) backButton.getScene().getWindow();
        stages.close();

        Parent root = FXMLLoader.load(getClass().getResource("window_main.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Second Screen");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onClickAdvanceSearch(ActionEvent event) throws SQLException {

        String selectedBrand = brandCombobox.getValue();
        String selectedProduct = productCombobox.getValue();
        ResultSet resultSet;

        if(modelTextField.getText().equals(""))
            resultSet= db.getResults("SELECT * FROM PRODUCT WHERE BRAND = '"+selectedBrand+"' AND TYPE = '"+selectedProduct+"';");
        else
            resultSet= db.getResults("SELECT * FROM PRODUCT WHERE BRAND = '"+selectedBrand+"' AND TYPE = '"+selectedProduct+"' AND MODEL = '"+modelTextField.getText()+"';");

        setOnTable(getObservableList(resultSet));
    }

    @FXML
    public void onClickSearchButton(ActionEvent event) throws SQLException {
        ResultSet resultSet = db.getResults("SELECT * FROM PRODUCT WHERE BRAND = '"+searchTextField.getText().toUpperCase()+"' OR TYPE = '"
                +searchTextField.getText().toUpperCase()+"' OR MODEL = '" +searchTextField.getText().toUpperCase()+"' OR SERIAL = '"
                +searchTextField.getText().toUpperCase()+"';");

        setOnTable(getObservableList(resultSet));
    }

    private ObservableList<OnSaleProduct> getObservableList (ResultSet resultSet) throws SQLException{
        ObservableList<OnSaleProduct> list = FXCollections.observableArrayList();

        while(resultSet.next()) {
            String ID = resultSet.getString("ID");
            String distro = resultSet.getString("DISTRIBUTOR");
            String type = resultSet.getString("TYPE");
            String size = resultSet.getString("SIZE");
            String brand = resultSet.getString("BRAND");
            String model = resultSet.getString("MODEL");
            String serial = resultSet.getString("SERIAL");
            String quantity = resultSet.getString("QUANTITY");
            String price = resultSet.getString("PRICE");
            String rPrice = resultSet.getString("RPRICE");
            String date = resultSet.getString("DATE");

            list.add(new OnSaleProduct(ID, distro, type, size, brand, model, serial, quantity, price, rPrice, date));
        }

        return list;
    }

    private void setOnTable(ObservableList<OnSaleProduct> list) {
        idOfProduct.setCellValueFactory(new PropertyValueFactory<>("ID"));
        typeOfProduct.setCellValueFactory(new PropertyValueFactory<>("type"));
        sizeOfProduct.setCellValueFactory(new PropertyValueFactory<>("size"));
        brandOfProduct.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelOfProduct.setCellValueFactory(new PropertyValueFactory<>("model"));
        serialOfProduct.setCellValueFactory(new PropertyValueFactory<>("serial"));
        quantityOfProduct.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceOfProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        realPriceOfProduct.setCellValueFactory(new PropertyValueFactory<>("rPrice"));

        tableView.setItems(list);
    }

    @FXML
    public void onClickAddToCartButton(ActionEvent event) {
        OnSaleProduct selectedIndex = tableView.getSelectionModel().getSelectedItem();
        if(selectedIndex != null) {

            list.add(selectedIndex);
            StringTokenizer stk = new StringTokenizer(cartLabel.getText());
            String token = "";

            while (stk.hasMoreTokens())
                token = stk.nextToken();

            int i = Integer.parseInt(token);
            i= i+1;

            cartLabel.setText("Currently in Cart "+Integer.toString(i));

        }
    }

    @FXML
    public void onClickViewCartButton(ActionEvent event) throws IOException, SQLException{
        db.close();
        if(list != null)
            new Parser(list);

        Stage scene1 = (Stage) backButton.getScene().getWindow();
        scene1.close();

        Parent root = FXMLLoader.load(getClass().getResource("purchase_screen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            db = new DatabaseHelper();
            db.executeQuery("DELETE FROM PRODUCT WHERE QUANTITY ='0'");
            setComboBox();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private ObservableList<String> getBrandCombobox() throws SQLException {
        ResultSet r = db.getResults("SELECT DISTINCT BRAND FROM PRODUCT");
        ObservableList<String> list = FXCollections.observableArrayList();
        while (r.next())
            list.add(r.getString("BRAND"));

        return list;
    }

    @FXML
    public void onSelectBrandComboBox(ActionEvent event) throws SQLException {
        ResultSet r = db.getResults("SELECT DISTINCT TYPE FROM PRODUCT WHERE BRAND ='"+brandCombobox.getValue()+"'");
        ObservableList<String> list = FXCollections.observableArrayList();
        while (r.next())
            list.add(r.getString("TYPE"));

        productCombobox.setItems(list);
    }

    private ObservableList<String> getProductCombobox() throws SQLException {
        System.out.println(brandCombobox.getValue());
        ResultSet r = db.getResults("SELECT DISTINCT TYPE FROM PRODUCT;");
        ObservableList<String> list = FXCollections.observableArrayList();
        while (r.next())
            list.add(r.getString("TYPE"));

        return list;
    }

    private void setComboBox() throws SQLException {
        brandCombobox.setItems(getBrandCombobox());
        productCombobox.setItems(getProductCombobox());
    }
}

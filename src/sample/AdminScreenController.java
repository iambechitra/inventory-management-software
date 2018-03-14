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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {
    private ObservableList<Product> additionProduct = FXCollections.observableArrayList();
    private String tempQuantity = "0.0";
    private String tempPrice = "0.0";

    @FXML Button backToMainButton, commitButton, totalAssetButton;

    @FXML TextField distro, type, size, brand, model, serial, quantity, price, rPrice, searchTextField;

    @FXML TableView<Product> tableView;

    @FXML
    DatePicker datePicker;

    private DatabaseHelper db;

    @FXML TableColumn<Product, String> distributorOfProduct, typeOfProduct, sizeOfProduct, brandOfProduct, modelOfProduct, serialOfProduct, quantityOfProduct, priceOfProduct, realPriceOfProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = new DatabaseHelper();
            db.executeQuery("DELETE FROM PRODUCT WHERE QUANTITY ='0'");
            totalAssetButton.setText("$ "+getInitialAsset().toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private void setAssetOnButton(BigDecimal temp) throws SQLException{
        BigDecimal priv = getInitialAsset();

        BigDecimal price1 = new BigDecimal(price.getText());
        price1 = price1.multiply(new BigDecimal(quantity.getText()));
        price1 = price1.subtract(temp);
        price1 = price1.add(priv);

        db.executeQuery("UPDATE TOTAL_ASSET SET ASSET ='"+price1.toString()+"' WHERE ASSET ='"+priv.toString()+"'");

        totalAssetButton.setText("$ "+price1.toString());
    }

    private BigDecimal getInitialAsset() throws SQLException {
        ResultSet r = db.getResults("SELECT * FROM total_asset");
        BigDecimal priv = new BigDecimal("0.0");
        while(r.next())
            priv = new BigDecimal(r.getString("ASSET"));

        return priv;
    }

    @FXML
    public void onClickAllDataButton(ActionEvent event) throws SQLException {
        ResultSet r = db.getResults("SELECT * FROM PRODUCT");
        setOnTable(getObservableList(r));
    }

    @FXML
    public void setOnClickQuantity(ActionEvent event) {
        if(!quantity.getText().equals(""))
            tempQuantity = quantity.getText();
    }

    @FXML
    public void setOnClickPrice(ActionEvent event) {
        if(!price.getText().equals(""))
            tempPrice = price.getText();
    }

    @FXML
    public void onClickTotalAssetButton (ActionEvent event) throws SQLException{
        //TODO
    }

    @FXML
    public void onClickBackToMainButton(ActionEvent event) throws IOException, SQLException {
        db.close();
        Stage stage1 = (Stage)backToMainButton.getScene().getWindow();
        stage1.close();

        Parent root = FXMLLoader.load(getClass().getResource("window_main.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onClickCommitButton(ActionEvent event) throws  SQLException {
        if(!commitButton.getText().equals("Update")) {
            if (!(distro.getText().equals("") && type.getText().equals("") && brand.getText().equals("") &&
                    model.getText().equals(""))) {

                if(serial.getText().equals(""))
                    serial.setText("NULL");

                if(quantity.getText().equals(""))
                    quantity.setText("1");

                if(size.getText().equals(""))
                    size.setText("NULL");

                if(price.getText().equals(""))
                    price.setText("0");

                if (rPrice.getText().equals(""))
                    rPrice.setText("0");

                if(model.getText().equals(""))
                    model.setText("NULL");

                db.executeQuery("insert into product(distributor, type, size, brand, model, serial, quantity, price, rprice, date) values ('" + distro.getText().toUpperCase() + "', '" +
                        type.getText().toUpperCase() + "', '"+size.getText().toUpperCase()+"', '" + brand.getText().toUpperCase() + "', '" + model.getText().toUpperCase() + "', '" + serial.getText().toUpperCase() + "', '" + quantity.getText()
                        + "', '" + price.getText() + "', '" + rPrice.getText() + "', '"+datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+"')");

                additionProduct.add(new Product(distro.getText(), type.getText(), size.getText(), brand.getText(), model.getText(), serial.getText()
                        , quantity.getText(), price.getText(), rPrice.getText(), datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                setOnTable(additionProduct);


                setAssetOnButton(BigDecimal.ZERO);
            }
        } else {
            Product product = tableView.getSelectionModel().getSelectedItem();

            db.executeQuery("UPDATE PRODUCT SET DISTRIBUTOR = '"+distro.getText().toUpperCase()+"', TYPE = '"+type.getText().toUpperCase()+"', SIZE ='"+size.getText().toUpperCase()+"', BRAND = '"+brand.getText().toUpperCase()+"', MODEL ='"+
                model.getText().toUpperCase()+"', SERIAL='"+serial.getText().toUpperCase()+"', QUANTITY='"+quantity.getText()+"', PRICE='"+price.getText()+"', RPRICE='"+
                    rPrice.getText()+"' WHERE DISTRIBUTOR ='"+product.getDistro()+"' AND TYPE ='"+product.getType()+"' AND BRAND = '"+product.getBrand()+
                        "' AND MODEL = '"+product.getModel()+"' AND SERIAL ='"+product.getSerial()+"' AND QUANTITY = '"+product.getQuantity()+"' AND PRICE='"+
                            product.getPrice()+"' AND RPRICE ='"+product.getRPrice()+"' AND DATE ='"+datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+"';");

            setAssetOnButton(new BigDecimal(tempPrice).multiply(new BigDecimal(tempQuantity)));
            setOnTable(getObservableListFromPanel());

            distro.setText("");
            type.setText("");
            size.setText("");
            brand.setText("");
            model.setText("");
            serial.setText("");
            quantity.setText("");
            price.setText("");
            rPrice.setText("");

            commitButton.setText("Commit");

        }

    }

    @FXML
    private void onClickCustomerDataButton(ActionEvent event) throws SQLException, IOException {
        db.close();
        Stage stage1 =  (Stage) backToMainButton.getScene().getWindow();
        stage1.close();

        Parent root = FXMLLoader.load(getClass().getResource("customer_screen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }


    private ObservableList<Product> getObservableListFromPanel() {
        ObservableList<Product> product = FXCollections.observableArrayList();

        product.add(new Product(distro.getText(), type.getText(), size.getText(), brand.getText(), model.getText(), serial.getText()
                , quantity.getText(), price.getText(), rPrice.getText(), datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

        return product;
    }

    @FXML
    public void onClickSearchButton(ActionEvent event) throws SQLException{
        ResultSet resultSet = db.getResults("SELECT * FROM PRODUCT WHERE ID = '"+searchTextField.getText() +"' OR BRAND = '"+searchTextField.getText().toUpperCase()+"' OR TYPE = '"
                +searchTextField.getText().toUpperCase()+"' OR MODEL = '" +searchTextField.getText().toUpperCase()+"' OR SERIAL = '"
                +searchTextField.getText().toUpperCase()+"' OR DISTRIBUTOR = '"+searchTextField.getText().toUpperCase()+"';");

        setOnTable(getObservableList(resultSet));
    }

    private void onDemandUpdate(String distro, String type, String size, String brand, String model, String serial) {

    }


    private String getRepeatedDataID(String distro, String type, String size, String brand, String model, String serial) throws SQLException{
        ResultSet r = db.getResults(String.format("SELECT ID FROM PRODUCT WHERE DISTRIBUTOR = '%s' AND TYPE = '%s' AND SIZE" +
                "='%s' AND BRAND = '%s' AND MODEL = '%s' AND SERIAL = '%s'",distro, type, size, brand, model, serial));

        String id = "";
        while (r.next())
            id = r.getString("ID");

        return id;

    }

    public ObservableList<Product> getObservableList(ResultSet resultSet) throws SQLException{
        ObservableList<Product> list = FXCollections.observableArrayList();

        while(resultSet.next()) {
            list.add(new Product(resultSet.getString("DISTRIBUTOR"), resultSet.getString("TYPE"), resultSet.getString("SIZE"),resultSet.getString("BRAND"),
                    resultSet.getString("MODEL"), resultSet.getString("SERIAL"), resultSet.getString("QUANTITY"),
                    resultSet.getString("PRICE"), resultSet.getString("RPRICE"), resultSet.getString("DATE")));
        }

        return list;
    }

    @FXML
    public void setOnClickMoveButton(ActionEvent event) {
        if(tableView.getSelectionModel().getSelectedItem() != null) {
            commitButton.setText("Update");
            Product product = tableView.getSelectionModel().getSelectedItem();
            tempQuantity = product.getQuantity();
            tempPrice = product.getPrice();
            setValueOnPanel(product);
        }
    }

    @FXML
    public void onClickDeleteButton(ActionEvent event) throws SQLException {
        Product product = tableView.getSelectionModel().getSelectedItem();
        db.executeQuery("DELETE FROM PRODUCT WHERE DISTRIBUTOR ='"+product.getDistro()+"' AND TYPE ='"+product.getType()+"' AND SIZE ='"+product.getSize()+"' AND BRAND ='"+product.getBrand()+
            "' AND MODEL='"+product.getModel()+"' AND SERIAL ='"+product.getSerial()+"' AND QUANTITY='"+product.getQuantity()+"' AND PRICE='"+product.getPrice()+
                "' AND RPRICE ='"+product.getRPrice()+"';");

        tableView.getItems().removeAll(product);
    }

    private void setValueOnPanel(Product product) {
        distro.setText(product.getDistro());
        type.setText(product.getType());
        size.setText(product.getSize());
        brand.setText(product.getBrand());
        model.setText(product.getModel());
        serial.setText(product.getSerial());
        quantity.setText(product.getQuantity());
        price.setText(product.getPrice());
        rPrice.setText(product.getRPrice());
        datePicker.setValue(LocalDate.parse(product.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    private void setOnTable(ObservableList<Product> list) {
        distributorOfProduct.setCellValueFactory(new PropertyValueFactory<>("distro"));
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
}

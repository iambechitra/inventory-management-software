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
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class PurchaseScreenController implements Initializable{
    @FXML
    Button confirmOrderButton;

    @FXML
    Label errorMassageLabel, successLabel;

    @FXML
    TextField customerName, phoneNumber, address, totalPriceTextField;

    private ArrayList<String> priceList = new ArrayList<>();

    private DatabaseHelper db;

    private ObservableList<TreeItem<OnSaleProduct>> treeItems = FXCollections.observableArrayList();
    TreeItem<OnSaleProduct> root = new TreeItem<>(new OnSaleProduct());

    @FXML
    TreeTableView<OnSaleProduct> treeTableView;

    @FXML
    TreeTableColumn<OnSaleProduct, String> idOfProduct, brandOfProduct, typeOfProduct, modelOfProduct, sizeOfProduct, serialOfProduct, quantityOfProduct, priceOfProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Parser p = new Parser();
        for(int i = 0; i < p.getList().size(); i++)
            priceList.add(p.getList().get(i).getPrice());

        setOnTreeItems(p.getList());
        setColumnValue();
        root.getChildren().setAll(treeItems);
        makeTableEditable();
        treeTableView.setEditable(true);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        try {
            db = new DatabaseHelper();
        } catch (Exception e) {
            System.err.println("Purchase Screen Error: "+e.getMessage());
        }
    }

    private void makeTableEditable() {
        quantityOfProduct.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        quantityOfProduct.setOnEditCommit(event -> {
            TreeItem<OnSaleProduct> currentEditingItems= treeTableView.getTreeItem(event.getTreeTablePosition().getRow());
            currentEditingItems.getValue().setQuantity(event.getNewValue());
        });

        priceOfProduct.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        priceOfProduct.setOnEditCommit(event -> {
            TreeItem<OnSaleProduct> currentEditingItems= treeTableView.getTreeItem(event.getTreeTablePosition().getRow());
            currentEditingItems.getValue().setPrice(event.getNewValue());
        });
    }

    private void setColumnValue(){
        idOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getID()));
        brandOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getBrand()));
        typeOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getType()));
        modelOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getModel()));
        sizeOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSize()));
        serialOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSerial()));
        quantityOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getQuantity()));
        priceOfProduct.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPrice()));
    }

    public void setOnTreeItems(ObservableList<OnSaleProduct> list) {
        for(int i = 0; i < list.size(); i++)
            treeItems.add(new TreeItem<>(list.get(i)));

    }

    @FXML
    public void onClickConfirmOrderButton(ActionEvent event) throws IOException, SQLException {
        if(!(customerName.getText().equals("") && phoneNumber.getText().equals("") && address.getText().equals(""))) {
            ObservableList<TreeItem<OnSaleProduct>> items = treeTableView.getRoot().getChildren();
            for (int i = 0; i < items.size(); i++) {
                if((Integer.parseInt(items.get(i).getValue().getQuantity()) > Integer.parseInt(getQuantity(items.get(i).getValue().getID()))) && Integer.parseInt(items.get(i).getValue().getQuantity()) > 0) {
                    errorMassageLabel.setText("Something went wrong, Check all the field.");
                    return;
                }
            }

            BigDecimal toDeduct = new BigDecimal("0");
            BigDecimal total = new BigDecimal("0");

            for(int i = 0; i < items.size(); i++) {
                OnSaleProduct onSaleProduct = items.get(i).getValue();
                int q = Integer.parseInt(onSaleProduct.getQuantity());
                int initq = Integer.parseInt(getQuantity(onSaleProduct.getID()));
                initq -= q;

                db.executeQuery("UPDATE PRODUCT SET QUANTITY ='"+Integer.toString(initq)+"' WHERE ID ="+onSaleProduct.getID());
                db.executeQuery(String.format("INSERT INTO CUSTOMER VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        customerName.getText().toUpperCase(), phoneNumber.getText().toUpperCase(), address.getText().toUpperCase(), onSaleProduct.getDistro(),
                        onSaleProduct.getType(), onSaleProduct.getSize(), onSaleProduct.getBrand(), onSaleProduct.getModel(),
                        onSaleProduct.getSerial(), onSaleProduct.getQuantity(), onSaleProduct.getPrice(), onSaleProduct.getRPrice(),
                        onSaleProduct.getDate(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())));

                toDeduct = toDeduct.add(new BigDecimal(priceList.get(i)).multiply(new BigDecimal(onSaleProduct.getQuantity())));
                total = total.add(new BigDecimal(onSaleProduct.getPrice()).multiply(new BigDecimal(onSaleProduct.getQuantity())));
            }

            BigDecimal asset = new BigDecimal(getAsset());

            BigDecimal bigDecimal = asset;
            bigDecimal = bigDecimal.subtract(toDeduct);

            db.executeQuery("UPDATE TOTAL_ASSET SET ASSET ='"+bigDecimal.toString()+"' WHERE ASSET ='"+asset.toString()+"'");

            totalPriceTextField.setText(total.toString()+" TK");
            successLabel.setText("Success!");
        }

    }

    private String getAsset() throws SQLException {
        ResultSet r = db.getResults("SELECT ASSET from total_asset");
        String asset = "0";
        while (r.next())
            asset = r.getString("ASSET");

        return asset;
    }

    private String getQuantity(String id) throws SQLException{
        ResultSet resultSet = db.getResults("SELECT QUANTITY FROM PRODUCT WHERE ID ='"+id+"'");
        String string = "";
        while (resultSet.next())
            string = resultSet.getString("QUANTITY");

        return string;
    }

    @FXML
    public void onClickCancelButton(ActionEvent event) throws IOException, SQLException{
        db.close();
        Stage stage1 = (Stage) confirmOrderButton.getScene().getWindow();
        stage1.close();

        Parent root = FXMLLoader.load(getClass().getResource("selling_window.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }
}

package sample;

import javafx.beans.property.SimpleStringProperty;

public class OnSaleProduct extends Product{

    private SimpleStringProperty ID;

    OnSaleProduct() {

    }

    OnSaleProduct(String ID, String distro, String type, String size, String brand, String model, String serial, String quantity, String price, String rPrice, String date) {
        super(distro, type, size, brand, model, serial, quantity, price, rPrice, date);
        this.ID = new SimpleStringProperty(ID);
    }

    public String getID() {
        return ID.get();
    }
}

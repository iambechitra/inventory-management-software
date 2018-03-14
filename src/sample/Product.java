package sample;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty distro, type, size, brand, model, serial, quantity, price, rPrice, date;

    Product() {

    }

    Product(String distro, String type, String size, String brand, String model, String serial, String quantity, String price, String rPrice, String date) {
        this.distro = new SimpleStringProperty(distro);
        this.type = new SimpleStringProperty(type);
        this.size = new SimpleStringProperty(size);
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.serial = new SimpleStringProperty(serial);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
        this.rPrice = new SimpleStringProperty(rPrice);
        this.date = new SimpleStringProperty(date);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getDistro() { return distro.get(); }
    public String getSerial() { return serial.get(); }
    public String getType() { return type.get(); }
    public String getSize() { return size.get(); }
    public String getBrand() { return brand.get(); }
    public String getModel() { return model.get(); }
    public String getQuantity() { return quantity.get(); }
    public String getPrice() { return  price.get(); }
    public String getRPrice() { return rPrice.get(); }
    public String getDate() { return date.get(); }
}

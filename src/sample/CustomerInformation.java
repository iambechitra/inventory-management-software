package sample;

import javafx.beans.property.SimpleStringProperty;

public class CustomerInformation extends Product {
    private SimpleStringProperty name, phoneNumber, address, sysDate;
    CustomerInformation() { }

    CustomerInformation(String name, String phoneNumber, String address, String distro, String type, String size, String brand, String model, String serial, String quantity, String price, String rPrice, String date, String sysDate) {
        super(distro, type, size, brand, model, serial, quantity, price, rPrice, date);
        this.name = new SimpleStringProperty(name);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.address = new SimpleStringProperty(address);
        this.sysDate = new SimpleStringProperty(sysDate);
    }

    public String getAddress() {
        return address.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getSysDate() {
        return sysDate.get();
    }

    public String getName() {
        return name.get();
    }
}

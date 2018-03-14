package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Parser {
    private static ObservableList<OnSaleProduct> list = FXCollections.observableArrayList();

    Parser() {

    }

    Parser(ObservableList<OnSaleProduct> list) {
        this.list = list;
    }

    public ObservableList<OnSaleProduct> getList() { return this.list; }
}

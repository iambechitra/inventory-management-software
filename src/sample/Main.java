package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.ResultSet;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("window_main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        DatabaseHelper db = new DatabaseHelper();
        //db.executeQuery("drop table if exists product");
        try {
            db.executeQuery("create table customer(name string, phone string, address string, distributor string, type string, size string, brand string, model string, serial string, quantity string, price string, rprice string, date string, sysdate string)");
            db.executeQuery("create table product(id integer primary key autoincrement, distributor string, type string, size string, brand string, model string, serial string, quantity string, price string, rprice string, date string)");
            db.executeQuery("create table total_asset(asset string)");
            db.executeQuery("insert into total_asset values('0.0')");
            db.close();
        } catch (Exception e) {
            db.close();
        }
        //db.executeQuery("create table serial(id integer, serialnumber string )");
        /*db.executeQuery("create table temp( id integer )");
        db.executeQuery("CREATE TRIGGER aft_insert AFTER INSERT ON product\n" +
                "BEGIN\n" +
                "INSERT INTO temp(id)\n" +
                "         VALUES(NEW.id);\n" +
                "END;");*/

        //db.executeQuery("insert into product(distributor, type, brand, model, price, rprice) values ('AR', 'SSD', 'ACER', 'WD67', '1200', '1000')");
        /*ResultSet r  = db.getResults("Select * from temp");

        while(r.next()){
            System.out.println(r.getInt("id"));
        }*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}

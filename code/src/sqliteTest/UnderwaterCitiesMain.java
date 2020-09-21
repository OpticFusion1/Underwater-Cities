package sqliteTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UnderwaterCitiesMain {
    private MainController mainController;
    public static void main(String a[]){
        UnderwaterCitiesMain underwaterCitiesMain = new UnderwaterCitiesMain();

            //System.out.println("Creating the map... Setting up the Story");
            underwaterCitiesMain.mainController = new MainController("");
            underwaterCitiesMain.mainController.run();






    }
}


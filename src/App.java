import java.sql.*;

import Functions.sqlFunctions;
import Functions.Controller;


/*
    ER Diagram: https://drive.google.com/file/d/1mkx9NiLk2uK2gIdeSuqhW2W41UT9KF79/view?usp=sharing
*/

public class App {
    public static void main(String[] args) throws Exception {
            Controller cmd = new Controller();
            System.out.println("Connecting to database...");
            if (cmd.run()) {
                try {
                    cmd.Menu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}

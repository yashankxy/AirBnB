import java.sql.*;

import Functions.sqlFunctions;
import Functions.Controller;


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

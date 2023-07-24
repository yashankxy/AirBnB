package Functions;

import java.util.Scanner;

public class Controller {
    Scanner sc = null;
    sqlFunctions db = null;

    public boolean run (){
        if (sc == null)
            sc = new Scanner(System.in);
        if (db == null)
            db = new sqlFunctions();
            db.connect();
        return true;
    }
    public boolean close(){
        if (db != null)
            db.disconnect();
        return true;
    }    
}

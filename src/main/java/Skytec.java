import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Skytec {
    private final String password = "postgres";
    private final String user = "postgres";
    private final String url = "jdbc:postgresql://localhost:5432/skytec_task";

    public static void main(String[] args) {
        Skytec skytec = new Skytec();

    }

}
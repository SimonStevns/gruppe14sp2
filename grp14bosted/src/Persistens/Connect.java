package Persistens;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Connect {

    public static final String BOSTED_URL = "jdbc:mysql://localhost/grp14bosted?useUnicode=yes&characterEncoding=UTF-8";
    public static final String BORGER_URL = "jdbc:mysql://localhost/borger?useUnicode=yes&characterEncoding=UTF-8";
    public static final String MEDICIN_URL = "jdbc:mysql://localhost/medicin?useUnicode=yes&characterEncoding=UTF-8";

    private final String USER, PASS, JDBC_DRIVER, URL;
    private Connection conn;

    public Connect(String url, String username, String password) {
        this.JDBC_DRIVER = "com.mysql.jdbc.Driver";

        this.URL = url;
        this.USER = username;
        this.PASS = "";
        this.conn = null;
    }

    public void openConnection() {
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);

        } catch (Exception e) {
            //Handle errors for JDBC
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public ResultSet query(String sqlQuery) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sqlQuery);
    }
}

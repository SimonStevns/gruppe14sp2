package Persistens;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

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

    public void openConnection() throws SQLTimeoutException, SQLException{
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
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
    
    public void update(String sql) throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public ResultSet query(String sqlQuery) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sqlQuery);
    }
    
    public PreparedStatement getPreparedstmt(String sql){
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt;
        } catch (SQLException ex) {}
        
        return null;
    }
}

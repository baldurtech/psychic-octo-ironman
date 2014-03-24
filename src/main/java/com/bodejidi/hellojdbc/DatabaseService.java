package com.bodejidi.hellojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseService {

    static final String jdbcUrl = "jdbc:mysql://localhost/hellojdbc?user=root&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DatabaseService() {

    }

    public ResultSet executeQuery(String sql) {
        try {
            conn = createConnection();
            stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch(SQLException e) {

        }
        return null;
    }

    public void close() {
        close(rs);
        rs = null;

        close(stmt);
        stmt = null;

        close(conn);
        conn = null;
    }

    protected Connection createConnection() throws SQLException {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName(jdbcDriver).newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        return DriverManager.getConnection(jdbcUrl);
    }

    protected void close(AutoCloseable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}

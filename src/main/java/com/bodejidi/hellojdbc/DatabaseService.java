package com.bodejidi.hellojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

public class DatabaseService {

    static final String jdbcUrl = "jdbc:mysql://localhost/hellojdbc?user=root&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    private DatabaseService() {
    }

    static public DatabaseService newInstance()
        throws SQLException {

        DatabaseService databaseService = new DatabaseService();

        databaseService.conn = databaseService.createConnection();
        databaseService.stmt = databaseService.conn.createStatement();

        return databaseService;
    }

    public DatabaseService prepare(String sql) {
        return this;
    }

    public DatabaseService setString(String param) {
        return this;
    }

    public DatabaseService setDate(Date param) {
        return this;
    }

    public void execute() {
    }

    public ResultSet executeQuery(String sql)
        throws SQLException {

        return stmt.executeQuery(sql);
    }

    public void execute(String sql)
        throws SQLException {

        stmt.execute(sql);
    }

    public void close() {
        close(rs);
        rs = null;

        close(stmt);
        stmt = null;

        close(pstmt);
        pstmt = null;

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

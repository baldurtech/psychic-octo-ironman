package com.bodejidi.hellojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

public class DatabaseService {

    static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    static final String jdbcUrl = "jdbc:mysql://localhost/hellojdbc?user=root&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    private Integer parameterIndex;

    private DatabaseService() {
    }

    static public DatabaseService newInstance() {

        DatabaseService databaseService = new DatabaseService();

        try {
            databaseService.conn = databaseService.createConnection();
            databaseService.stmt = databaseService.conn.createStatement();
        } catch(SQLException e) {
            logger.error("Cannot init databaseService", e);
        }
        return databaseService;
    }

    public DatabaseService prepare(String sql) throws SQLException {
        logger.trace("Prepare statement: " + sql);
        pstmt = conn.prepareStatement(sql);
        parameterIndex = 1;
        return this;
    }

    public DatabaseService setString(String param) throws SQLException {
        logger.trace("Set parameter value " + parameterIndex + " to String '" + param + "'");
        pstmt.setString(parameterIndex, param);
        parameterIndex ++;
        return this;
    }

    public DatabaseService setLong(Long param) throws SQLException {
        logger.trace("Set parameter value " + parameterIndex + " to Long '" + param + "'");
        pstmt.setLong(parameterIndex, param);
        parameterIndex ++;
        return this;
    }

    public DatabaseService setDate(Date date) throws SQLException {
        logger.trace("Set parameter value " + parameterIndex + " to Date '" + date + "'");
        pstmt.setDate(parameterIndex, new java.sql.Date(date.getTime()));
        parameterIndex ++;
        return this;
    }

    public Boolean execute() throws SQLException {
        logger.debug("Execute preparedStatement " + pstmt);
        return pstmt.execute();
    }

    public ResultSet executeQuery() throws SQLException {
        logger.debug("Execute preparedStatement " + pstmt);
        return pstmt.executeQuery();
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

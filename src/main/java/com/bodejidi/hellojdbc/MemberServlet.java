package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.AutoCloseable;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class MemberServlet extends HttpServlet {

    static final String jdbcUrl = "jdbc:mysql://localhost/hellojdbc?user=root&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";

    static final String contentType = "text/html; charset=UTF-8";

    static final String MEMBER_TABLE = "member";
    static final String MEMBER_ID = "id";
    static final String MEMBER_FIRST_NAME = "first_name";
    static final String MEMBER_LAST_NAME = "last_name";
    static final String MEMBER_FORM_ID = "id";
    static final String MEMBER_FORM_FIRST_NAME = "first_name";
    static final String MEMBER_FORM_LAST_NAME = "last_name";
    static final String FORM_SUBMIT_ACTION = "action";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType(contentType);

        PrintWriter out = resp.getWriter();

        String paramId = req.getParameter(MEMBER_FORM_ID);
        if(null == paramId) {
            list(req, resp);
        } else {
            show(req, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType(contentType);
        String action = req.getParameter(FORM_SUBMIT_ACTION);

        PrintWriter out = resp.getWriter();

        String id = req.getParameter(MEMBER_FORM_ID);
        String firstName = req.getParameter(MEMBER_FORM_FIRST_NAME);
        String lastName = req.getParameter(MEMBER_FORM_LAST_NAME);

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = createConnection();
            stmt = conn.createStatement();

            if(id == null) {
                if(firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
                    String sql = "INSERT INTO " + MEMBER_TABLE+ "(" + MEMBER_FIRST_NAME + ", " + MEMBER_LAST_NAME + ", date_created, last_updated) "
                        + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
                    debug("SQL: " + sql);
                    stmt.execute(sql);
                    out.println("Add " + firstName + " " + lastName + " success!");
                } else {
                    out.println("Error: first name or last name cannot be empty.");
                }
                out.println("<br/><a href=\"\">Member List</a>");
            } else if ("Delete".equalsIgnoreCase(action)) {
                String sql = "DELETE FROM " + MEMBER_TABLE + " where " + MEMBER_ID + "=" + id;
                debug("SQL: " + sql);
                stmt.execute(sql);
                out.println("Delete ID=" + id + " success!");
                out.println("<br/><a href=\"\">Member List</a>");
            } else if("Update".equalsIgnoreCase(action)){
                String sql = "update " + MEMBER_TABLE + " set " + MEMBER_FIRST_NAME + "='" + firstName + "', " + MEMBER_LAST_NAME + "='" + lastName + "' where " + MEMBER_ID + "="+id;
                debug("SQL: " + sql);
                stmt.execute(sql);
                out.println("Update id=" + id + ": " + firstName + " " + lastName + " success!");
                out.println("<br/><a href=\"\">Member List</a>");
            }
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        } finally {
            close(stmt);
            stmt = null;

            close(conn);
            conn = null;
        }
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

    public void debug(String str) {
        System.out.println("[DEBUG] " + (new Date()) + " " + str);
    }

    public void list(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = createConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * from " + MEMBER_TABLE;
            debug("SQL: " + sql);
            rs = stmt.executeQuery(sql);
            out.println("<html><head><title>Member List</title></head>\n"
                        + "<body><h1>Member List</h1>\n"
                        + "<table border=\"1\"><tr><td>ID</td>"
                        + "<td>Name</td></tr>\n");
            while(rs.next()) {
                Long id = rs.getLong(MEMBER_ID);
                String firstName = rs.getString(MEMBER_FIRST_NAME);
                String lastName = rs.getString(MEMBER_LAST_NAME);
                out.println("<tr><td><a href=\"?id=" + id + "\">" + id
                            + "</a></td><td>" + firstName + " " + lastName
                            + "</td></tr>\n");
            }
            out.println("</table>");
            out.println("<p><a href=\".\">Add member</a></p>");
            out.println("</body></html>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        } finally {
            close(rs);
            rs = null;

            close(stmt);
            stmt = null;

            close(conn);
            conn = null;
        }
    }

    public void show(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = createConnection();
            stmt = conn.createStatement();
            String paramId = req.getParameter(MEMBER_FORM_ID);
            String sql = "SELECT * from " + MEMBER_TABLE;
            sql = sql + " WHERE " + MEMBER_ID + "=" + paramId;
            debug("SQL: " + sql);
            rs = stmt.executeQuery(sql);

            Member member = new Member();
            rs.next();
            member.setId(rs.getLong(MEMBER_ID));
            member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
            member.setLastName(rs.getString(MEMBER_LAST_NAME));

            out.println("<html><head><title>Member</title></head><body>"
                        + "  <h1>Member</h1>"
                        + "  <form action=\"member\" method=\"POST\">"
                        + "    <table border=\"1\">\n");

            out.println("      <tr><td>ID</td><td>" + member.getId() + "</td></tr>");

            out.println("      <tr><td>First Name</td><td>\n"
                        + "<input type=\"text\" name=\"first_name\""
                        + " value=\"" + member.getFirstName() + "\" /></td></tr>");

            out.println("      <tr><td>Last Name</td><td>\n"
                        + "<input type=\"text\" name=\"last_name\""
                        + " value=\"" + member.getLastName() + "\" /></td></tr>");

            out.println("    </table>");

            out.println("    <input type=\"hidden\" name=\"id\""
                        + " value=\"" + member.getId() + "\" />");

            out.println("    <input type=\"submit\" name=\"action\""
                        + " value=\"Update\" />");

            out.println("    <input type=\"submit\" name=\"action\""
                        + " value=\"Delete\" />");

            out.println("  </form>");
            out.println("  <p><a href=\"member\">Member list</a></p>");
            out.println("</body></html>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        } finally {
            close(rs);
            rs = null;

            close(stmt);
            stmt = null;

            close(conn);
            conn = null;
        }
    }

}

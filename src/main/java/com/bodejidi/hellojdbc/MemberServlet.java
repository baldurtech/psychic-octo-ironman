package com.bodejidi.hellojdbc;

import java.io.IOException;
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

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn =
                DriverManager.getConnection("jdbc:mysql://localhost/hellojdbc?"
                                            + "user=root"
                                            + "&password=");

            resp.setContentType("text/html; charset=UTF-8");
            stmt = conn.createStatement();
            String paramId = req.getParameter("id");
            String sql = "SELECT * from member";
            if(null == paramId) {
                System.out.println("SQL: " + sql);
                rs = stmt.executeQuery(sql);
                resp.getWriter().println("<html><head><title>Member List</title></head><body><h1>Member List</h1><table border=\"1\"><tr><td>ID</td><td>Name</td></tr>\n");
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    resp.getWriter().println("<tr><td>" + id + "</td><td>" + firstName + " " + lastName + "</td></tr>\n");
                }
                resp.getWriter().println("</table>");
                resp.getWriter().println("<p><a href=\".\">Add member</a></p>");
                resp.getWriter().println("</body></html>");
            } else {
                sql = sql + " WHERE id=" + paramId;
                System.out.println("SQL: " + sql);
                rs = stmt.executeQuery(sql);

                rs.next();
                Long id = rs.getLong("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                resp.getWriter().println("<html><head><title>Member</title></head><body>"
                                         + "  <h1>Member</h1>"
                                         + "  <table border=\"1\">\n");
                resp.getWriter().println("    <tr><td>ID</td><td>" + id + "</td></tr>\n");
                resp.getWriter().println("    <tr><td>First Name</td><td>" + firstName + "</td></tr>\n");
                resp.getWriter().println("    <tr><td>Last Name</td><td>" + lastName + "</td></tr>\n");
                resp.getWriter().println("  </table>");
                resp.getWriter().println("  <p><a href=\"member\">Member list</a></p>");
                resp.getWriter().println("</body></html>");
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            resp.getWriter().println("Error!");
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                conn = null;
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");

        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        Connection conn = null;
        Statement stmt = null;

        try {
            conn =
                DriverManager.getConnection("jdbc:mysql://localhost/hellojdbc?"
                                            + "user=root"
                                            + "&password=");
            stmt = conn.createStatement();
            String sql = "INSERT INTO member(first_name, last_name, date_created, last_updated) "
                + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
            System.out.println("SQL: " + sql);
            stmt.execute(sql);
            resp.getWriter().println("Add " + firstName + " " + lastName + " success!");
            resp.getWriter().println("<br/><a href=\"\">Member List</a>");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            resp.getWriter().println("Error!");
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                conn = null;
            }
        }
    }
}

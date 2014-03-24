package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.ResultSet;

public class MemberServlet extends HttpServlet {

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

        HttpSession session = req.getSession();

        String action = req.getParameter("action");
        if("Logout".equalsIgnoreCase(action)) {
            session.removeAttribute("memberId");
        }

        Long memberId = (Long)session.getAttribute("memberId");
        if(memberId == null) {
            login(req, resp);
            return;
        }

        if(req.getParameter(MEMBER_FORM_ID) == null) {
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

        try {
            if ("Login".equalsIgnoreCase(action)) {
                String username = req.getParameter("username");
                String password = req.getParameter("password");

                if(username.equalsIgnoreCase("admin") &&
                   password.equals("s3cr3t")) {
                    HttpSession session = req.getSession();
                    session.setAttribute("memberId", 0L);

                    showLoginSuccess(req, resp);
                } else {
                    showLoginFail(req, resp);
                }
                return;
            }

            if(id == null) {
                create(req, resp);
            } else if ("Delete".equalsIgnoreCase(action)) {
                String sql = "DELETE FROM " + MEMBER_TABLE + " where " + MEMBER_ID + "=" + id;
                debug("SQL: " + sql);

                DatabaseService ds = DatabaseService.newInstance();
                ds.execute(sql);
                ds.close();

                out.println("Delete ID=" + id + " success!");
                out.println("<br/><a href=\"\">Member List</a>");
            } else if("Update".equalsIgnoreCase(action)){
                String sql = "update " + MEMBER_TABLE + " set " + MEMBER_FIRST_NAME + "='" + firstName + "', " + MEMBER_LAST_NAME + "='" + lastName + "' where " + MEMBER_ID + "="+id;
                debug("SQL: " + sql);

                DatabaseService ds = DatabaseService.newInstance();
                ds.execute(sql);
                ds.close();

                out.println("Update id=" + id + ": " + firstName + " " + lastName + " success!");
                out.println("<br/><a href=\"\">Member List</a>");
            }
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }

    public void debug(String str) {
        System.out.println("[DEBUG] " + (new Date()) + " " + str);
    }

    public void login(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>Login</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Please Login!</h1>");
        out.println("    <form action=\"member\" method=\"POST\">");
        out.println("      <label>Username: <input type=\"text\" name=\"username\"/></label>");
        out.println("      <label>Password: <input type=\"password\" name=\"password\"/></label>");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Login\" />");
        out.println("    </form>");
        out.println("  </body>");
        out.println("</html>");
    }

    public String showLoginInfo() {
        return "Welcome, admin. <a href=\"member?action=Logout\">logout</a>";
    }

    public void list(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        try {
            out.println("<html><head><title>Member List</title></head>\n"
                        + "<body>\n"
                        + showLoginInfo()
                        + "<h1>Member List</h1>\n"
                        + "<table border=\"1\"><tr><td>ID</td>"
                        + "<td>Name</td></tr>\n");

            for(Member member: findAllMember()) {
                out.println("<tr><td><a href=\"?id=" + member.getId() + "\">"
                            + member.getId()
                            + "</a></td><td>" + member.getFirstName()
                            + " " + member.getLastName()
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
        }
    }

    public void show(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();
        try {
            String paramId = req.getParameter(MEMBER_FORM_ID);
            Member member = getMemberById(paramId);

            out.println("<html><head><title>Member</title></head><body>"
                        + showLoginInfo()
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
        }
    }

    public void showLoginFail(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>Login Fail!</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Login Fail!</h1>");
        out.println("  </body>");
        out.println("</html>");

    }

    public void showLoginSuccess(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>Login Success</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Welcome Admin!</h1>");
        out.println("    <a href=\"member\">Member list</a>");
        out.println("  </body>");
        out.println("</html>");
    }

    public void create(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        try{
            String firstName = req.getParameter(MEMBER_FORM_FIRST_NAME);
            String lastName = req.getParameter(MEMBER_FORM_LAST_NAME);

            if(firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
                String sql = "INSERT INTO " + MEMBER_TABLE+ "(" + MEMBER_FIRST_NAME + ", " + MEMBER_LAST_NAME + ", date_created, last_updated) "
                    + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
                debug("SQL: " + sql);
                DatabaseService ds = DatabaseService.newInstance();
                ds.execute(sql);
                ds.close();
                out.println("Add " + firstName + " " + lastName + " success!");
            } else {
                out.println("Error: first name or last name cannot be empty.");
            }
            out.println("<br/><a href=\"\">Member List</a>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error: Cannot create member!");
        }
    }

    public Member getMemberById(String paramId)
        throws SQLException {

        Member member = new Member();

        String sql = "SELECT * from " + MEMBER_TABLE;
        sql = sql + " WHERE " + MEMBER_ID + "=" + paramId;
        debug("SQL: " + sql);

        DatabaseService databaseService = DatabaseService.newInstance();

        ResultSet rs = databaseService.executeQuery(sql);

        rs.next();
        member.setId(rs.getLong(MEMBER_ID));
        member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
        member.setLastName(rs.getString(MEMBER_LAST_NAME));

        databaseService.close();

        return member;
    }

    public List<Member> findAllMember() throws SQLException {
        List<Member> memberList = new ArrayList<Member>();

        DatabaseService databaseService = DatabaseService.newInstance();

        String sql = "SELECT * from " + MEMBER_TABLE;
        debug("SQL: " + sql);

        ResultSet rs = databaseService.executeQuery(sql);

        while(rs.next()) {
            Member member = new Member();
            member.setId(rs.getLong(MEMBER_ID));
            member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
            member.setLastName(rs.getString(MEMBER_LAST_NAME));
            memberList.add(member);
        }

        databaseService.close();

        return memberList;
    }
}

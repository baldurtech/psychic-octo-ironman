package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.ResultSet;

public class MemberServlet extends HttpServlet {

    static final String MEMBER_TABLE = "member";
    static final String MEMBER_ID = "id";
    static final String MEMBER_FIRST_NAME = "first_name";
    static final String MEMBER_LAST_NAME = "last_name";
    static final String MEMBER_FORM_ID = "id";
    static final String MEMBER_FORM_FIRST_NAME = "first_name";
    static final String MEMBER_FORM_LAST_NAME = "last_name";
    static final String FORM_SUBMIT_ACTION = "action";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");
        if(null == action || "".equals(action)) {
            action = "List";
        }

        switch(action.toLowerCase()) {
        case "create" : create(req, resp); break;
        case "show"   : show(req, resp); break;
        case "list"   :
        default       :
            list(req, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter(FORM_SUBMIT_ACTION);
        try{
            if("Save".equalsIgnoreCase(action)) {
                save(req, resp);
            } else if ("Delete".equalsIgnoreCase(action)) {
                delete(req ,resp);
            } else if("Update".equalsIgnoreCase(action)){
                update(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Operation not allowed.");
            }
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
        }

    }

    public void debug(String str) {
        System.out.println("[DEBUG] " + (new Date()) + " " + str);
    }

    public String showLoginInfo(HttpServletRequest req) {
        return "Welcome, admin. <a href=\"" + req.getContextPath() + "/auth/logout\">logout</a>";
    }

    public void create(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        forward("create", req, resp);
    }

    public void list(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        try {
            req.setAttribute("memberList", findAllMember());
            forward("list", req, resp);
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
            Long paramId = Long.valueOf(req.getParameter(MEMBER_FORM_ID));
            Member member = getMemberById(paramId);
            req.setAttribute("member", member);
            forward("show", req, resp);
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }

    public void save(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        Member member = new Member();
        member.setFirstName(req.getParameter(MEMBER_FORM_FIRST_NAME));
        member.setLastName(req.getParameter(MEMBER_FORM_LAST_NAME));

        try {
            save(member);
            req.setAttribute("flash.message",
                             "Add " + member + " success!");
        } catch(Exception e) {
            req.setAttribute("flash.errorMessage",
                             "Error: first name or last name cannot be empty.");
        }
        forward("result", req, resp);
    }

    public Member save(Member member) throws Exception {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();

        if(firstName == null || firstName.length() == 0 || lastName == null || lastName.length() == 0) {
            throw new Exception("Member validator error!");
        }

        String sql = "INSERT INTO " + MEMBER_TABLE+ "(" + MEMBER_FIRST_NAME + ", " + MEMBER_LAST_NAME + ", date_created, last_updated) "
            + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
        debug("SQL: " + sql);
        DatabaseService ds = DatabaseService.newInstance();
        ds.execute(sql);
        ds.close();
        return member;
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException, SQLException {

        PrintWriter out = resp.getWriter();

        String id = req.getParameter(MEMBER_FORM_ID);
        deleteById(Long.valueOf(id));

        req.setAttribute("flash.message", "Delete ID=" + id + " success!");
        forward("result", req, resp);
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM " + MEMBER_TABLE + " where " + MEMBER_ID + "=" + id;
        debug("SQL: " + sql);

        DatabaseService ds = DatabaseService.newInstance();
        ds.execute(sql);
        ds.close();
    }

    public void update(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException, SQLException {

        PrintWriter out = resp.getWriter();

        Member member = new Member();
        member.setId(Long.valueOf(req.getParameter(MEMBER_FORM_ID)));
        member.setFirstName(req.getParameter(MEMBER_FORM_FIRST_NAME));
        member.setLastName(req.getParameter(MEMBER_FORM_LAST_NAME));

        update(member);

        req.setAttribute("flash.message",
                         "Update id=" + member.getId() + ": " + member + " success!");
        forward("result", req, resp);
    }

    public Member update(Member member) throws SQLException {
        Long id = member.getId();
        String firstName = member.getFirstName();
        String lastName = member.getLastName();

        String sql = "update " + MEMBER_TABLE + " set " + MEMBER_FIRST_NAME + "='" + firstName + "', " + MEMBER_LAST_NAME + "='" + lastName + "' where " + MEMBER_ID + "="+id;
        debug("SQL: " + sql);

        DatabaseService ds = DatabaseService.newInstance();
        ds.execute(sql);
        ds.close();

        return member;
    }

    /**
     * @Deprecated
     */
    public Member getMemberById(String paramId)
        throws SQLException {

        return getMemberById(Long.valueOf(paramId));
    }

    public Member getMemberById(Long paramId)
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

    public void forward(String page,
                        HttpServletRequest req,
                        HttpServletResponse resp)
        throws IOException, ServletException {

        String jsp = "/WEB-INF/member/" + page + ".jsp";
        getServletContext()
            .getRequestDispatcher(jsp)
            .forward(req, resp);
    }
}
